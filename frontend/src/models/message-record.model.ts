export interface IMessageRecord {
    id?: number;
    legacyID: string;
    type: string;
    created: string;
    author: string;
    content?: string;
}
