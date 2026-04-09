import { Module } from '@nestjs/common';
import { ElasticsearchModule } from '@nestjs/elasticsearch';
import { Reflector } from '@nestjs/core';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { RolesGuard } from './guards/roles.guard';

@Module({
  imports: [ElasticsearchModule.register({
    node: process.env.ELASTICSEARCH_NODE || 'http://localhost:9200',
  })],
  controllers: [AppController],
  providers: [AppService, RolesGuard, Reflector],
})
export class AppModule { }
