import {
  CanActivate,
  ExecutionContext,
  ForbiddenException,
  Injectable,
} from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { REQUIRED_SCOPES_KEY } from '../decorators/roles.decorator';

@Injectable()
export class RolesGuard implements CanActivate {
  constructor(private readonly reflector: Reflector) {}

  canActivate(context: ExecutionContext): boolean {
    const requiredScopes = this.reflector.getAllAndOverride<string[]>(
      REQUIRED_SCOPES_KEY,
      [context.getHandler(), context.getClass()],
    );

    // If no scopes are required, allow access
    if (!requiredScopes || requiredScopes.length === 0) {
      return true;
    }

    const request = context.switchToHttp().getRequest();

    // The API Gateway propagates the X-Scopes header from the JWT token.
    // Format: comma-separated list of permission codes, e.g. "audit:read,contract:write"
    const scopesHeader: string = request.headers['x-scopes'] ?? '';
    const userScopes = scopesHeader
      .split(',')
      .map((s: string) => s.trim())
      .filter((s: string) => s.length > 0);

    const hasScope = requiredScopes.some((scope) => userScopes.includes(scope));

    if (!hasScope) {
      throw new ForbiddenException(
        `Access denied. Required scope: ${requiredScopes.join(' or ')}`,
      );
    }

    return true;
  }
}
