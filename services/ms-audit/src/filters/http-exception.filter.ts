import {
  ArgumentsHost,
  Catch,
  ExceptionFilter,
  HttpException,
  HttpStatus,
  Logger,
} from '@nestjs/common';
import { Request, Response } from 'express';

interface ErrorBody {
  status: number;
  message: string;
  code: string;
}

/**
 * Global exception filter for ms-audit (NestJS).
 * Catches all exceptions and returns a uniform { status, message, code } response.
 * Never exposes stack traces or internal details to the client.
 */
@Catch()
export class GlobalExceptionFilter implements ExceptionFilter {
  private readonly logger = new Logger(GlobalExceptionFilter.name);

  catch(exception: unknown, host: ArgumentsHost): void {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const request = ctx.getRequest<Request>();

    const body = this.buildErrorBody(exception);

    if (body.status >= 500) {
      this.logger.error(
        `[${body.status}] ${request.method} ${request.url} — ${body.message}`,
        exception instanceof Error ? exception.stack : String(exception),
      );
    } else {
      this.logger.warn(
        `[${body.status}] ${request.method} ${request.url} — ${body.message}`,
      );
    }

    response.status(body.status).json(body);
  }

  private buildErrorBody(exception: unknown): ErrorBody {
    if (exception instanceof HttpException) {
      const status = exception.getStatus();
      const exResponse = exception.getResponse();

      // NestJS validation pipe returns { message: string[] } — join them
      let message: string;
      if (
        typeof exResponse === 'object' &&
        exResponse !== null &&
        'message' in exResponse
      ) {
        const raw = (exResponse as Record<string, unknown>).message;
        message = Array.isArray(raw) ? raw.join('; ') : String(raw);
      } else {
        message = exception.message;
      }

      const code = this.codeFromStatus(status);
      return { status, message, code };
    }

    // Unknown / unexpected errors — never leak internals
    return {
      status: HttpStatus.INTERNAL_SERVER_ERROR,
      message: 'An unexpected internal error occurred',
      code: 'INTERNAL_ERROR',
    };
  }

  private codeFromStatus(status: number): string {
    if (status === 400) return 'VALIDATION_ERROR';
    if (status === 401) return 'AUTHENTICATION_ERROR';
    if (status === 403) return 'FORBIDDEN';
    if (status === 404) return 'RESOURCE_NOT_FOUND';
    if (status >= 500) return 'INTERNAL_ERROR';
    return 'HTTP_ERROR';
  }
}
