import { Controller, Get, Query } from '@nestjs/common';
import { EventPattern, Payload } from '@nestjs/microservices';
import { AppService } from './app.service';
import { EventDTO } from './audit.dto';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) { }

  @Get('health')
  getHealth() {
    return { status: 'UP' };
  }

  @Get('audit')
  async getAuditEvents(
    @Query('contractId') contractId?: string,
    @Query('action') action?: string,
  ) {
    const filters = { contractId, action };
    return this.appService.getEvents(filters);
  }

  @EventPattern('audit.events')
  async handleIncomingEvent(@Payload() message: any) {
    const eventData: EventDTO = message.value || message;
    console.log('Evento recibido:', eventData);
    await this.appService.persistEvent(eventData);
  }
}
