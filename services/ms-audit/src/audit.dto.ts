export class EventDTO {
    timestamp: string;
    serviceOrigin: string;
    action: string;
    data: Record<string, any>;
}