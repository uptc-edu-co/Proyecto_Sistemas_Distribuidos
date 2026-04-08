import { Injectable, OnModuleInit, Logger } from '@nestjs/common';
import { ElasticsearchService } from '@nestjs/elasticsearch';
import { EventDTO } from './audit.dto';

const AUDIT_INDEX = 'audit';

@Injectable()
export class AppService implements OnModuleInit {
  private readonly logger = new Logger(AppService.name);

  constructor(private readonly elasticsearchService: ElasticsearchService) { }

  async onModuleInit() {
    try {
      const exists = await this.elasticsearchService.indices.exists({ index: AUDIT_INDEX });
      if (!exists) {
        await this.elasticsearchService.indices.create({
          index: AUDIT_INDEX,
          mappings: {
            properties: {
              timestamp: { type: 'date' },
              serviceOrigin: { type: 'keyword' },
              action: { type: 'keyword' },
              data: {
                type: 'object',
                properties: {
                  id: { type: 'keyword' },
                },
              },
            },
          },
        });
        this.logger.log(`Índice '${AUDIT_INDEX}' creado con mappings optimizados.`);
      } else {
        this.logger.log(`Índice '${AUDIT_INDEX}' ya existe, omitiendo creación.`);
      }
    } catch (error) {
      this.logger.error('Error al inicializar el índice de Elasticsearch:', error);
    }
  }

  async persistEvent(event: EventDTO) {
    try {
      await this.elasticsearchService.index({
        index: AUDIT_INDEX,
        document: event,
      });
      this.logger.log('Evento guardado correctamente en Elasticsearch');
    } catch (error) {
      this.logger.error('Error al guardar el evento en Elasticsearch:', error);
    }
  }

  async getEvents(filters: { contractId?: string; action?: string; serviceOrigin?: string; from?: number; size?: number }) {
    const must: any[] = [];

    if (filters.contractId) {
      must.push({ term: { 'data.id': filters.contractId } });
    }

    if (filters.action) {
      must.push({ term: { action: filters.action } });
    }

    if (filters.serviceOrigin) {
      must.push({ term: { serviceOrigin: filters.serviceOrigin } });
    }

    const query = must.length > 0 ? { bool: { must } } : { match_all: {} };
    const from = filters.from ?? 0;
    const size = filters.size ?? 20;

    try {
      const result = await this.elasticsearchService.search({
        index: AUDIT_INDEX,
        query,
        sort: [{ timestamp: { order: 'desc' } }],
        from,
        size,
      });
      return {
        total: (result.hits.total as any)?.value ?? result.hits.hits.length,
        from,
        size,
        results: result.hits.hits.map((hit) => hit._source),
      };
    } catch (error) {
      this.logger.error('Error al consultar Elasticsearch:', error);
      return { total: 0, from, size, results: [] };
    }
  }
}
