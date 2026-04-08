import { SetMetadata } from '@nestjs/common';

export const REQUIRED_SCOPES_KEY = 'requiredScopes';

/**
 * Decorator to annotate endpoints with required scopes.
 * Example: @RequireScopes('audit:read')
 */
export const RequireScopes = (...scopes: string[]) =>
  SetMetadata(REQUIRED_SCOPES_KEY, scopes);
