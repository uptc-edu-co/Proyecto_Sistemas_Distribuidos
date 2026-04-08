import { Controller, Get, Headers, Query, UseGuards } from '@nestjs/common';
import { EventPattern, Payload } from '@nestjs/microservices';
import { AppService } from './app.service';
import { EventDTO } from './audit.dto';
import { RequireScopes } from './decorators/roles.decorator';
import { RolesGuard } from './guards/roles.guard';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) { }

  @Get('health')
  getHealth() {
    return { status: 'UP' };
  }

  @Get('audit')
  @UseGuards(RolesGuard)
  @RequireScopes('view:audit')
  async getAuditEvents(
    @Headers('x-user') user: string,
    @Query('contractId') contractId?: string,
    @Query('action') action?: string,
    @Query('serviceOrigin') serviceOrigin?: string,
    @Query('from') from?: string,
    @Query('size') size?: string,
  ) {
    const filters = {
      contractId,
      action,
      serviceOrigin,
      from: from ? parseInt(from, 10) : undefined,
      size: size ? parseInt(size, 10) : undefined,
    };
    return this.appService.getEvents(filters);
  }

  @EventPattern('audit.events')
  async handleIncomingEvent(@Payload() message: any) {
    const eventData: EventDTO = message.value || message;
    console.log('Evento recibido:', eventData);
    await this.appService.persistEvent(eventData);
  }
}
