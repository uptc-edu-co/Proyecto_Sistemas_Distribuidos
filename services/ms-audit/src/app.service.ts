import { Injectable } from '@nestjs/common';
import { ElasticsearchService } from '@nestjs/elasticsearch';
import { EventDTO } from './audit.dto';

@Injectable()
export class AppService {
  constructor(private readonly elasticsearchService: ElasticsearchService) { }

  async persistEvent(event: EventDTO) {
    try {
      await this.elasticsearchService.index({
        index: 'audit',
        document: event,
      });
      console.log('Evento guardado correctamente en Elasticsearch');
    } catch (error) {
      console.error('Error al guardar el evento en Elasticsearch:', error);
    }
  }

  async getEvents(filters: any) {
    const must: any[] = [];

    if (filters.contractId) {
      must.push({
        match: {
          'data.id': filters.contractId
        }
      });
    }

    if (filters.action) {
      must.push({
        match: {
          action: filters.action
        }
      });
    }

    const query = must.length > 0 ? { bool: { must } } : { match_all: {} };

    try {
      const result = await this.elasticsearchService.search({
        index: 'audit',
        query,
        sort: [{ timestamp: { order: 'desc' } }],
      });
      return result.hits.hits.map((hit) => hit._source);
    } catch (error) {
      console.error('Error al consultar Elasticsearch:', error);
      return [];
    }
  }
}
