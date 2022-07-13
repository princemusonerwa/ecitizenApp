import { IComplain } from 'app/shared/model/complain.model';

export interface IAttachment {
  id?: number;
  attachmentLinkContentType?: string;
  attachmentLink?: string;
  attachmentType?: string;
  complain?: IComplain | null;
}

export const defaultValue: Readonly<IAttachment> = {};
