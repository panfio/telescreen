export interface IMessageRecord {
    id?: number;
    legacyID: string;
    type: string;
    created: string;
    author: string;
    content?: string;
}

export const defaultValue: Readonly<IMessageRecord> = {
    id: 0,
    legacyID: '',
    type: '',
    created: new Date(Date.now()).toISOString(),
    author: '',
    content: '',
};
