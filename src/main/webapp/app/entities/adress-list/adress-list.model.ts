export interface IAdressList {
  id?: number;
  street?: string | null;
  streetSuite?: string | null;
  postalCode?: string | null;
  state?: string | null;
  country?: string | null;
  latitude?: number | null;
  longitude?: number | null;
}

export class AdressList implements IAdressList {
  constructor(
    public id?: number,
    public street?: string | null,
    public streetSuite?: string | null,
    public postalCode?: string | null,
    public state?: string | null,
    public country?: string | null,
    public latitude?: number | null,
    public longitude?: number | null
  ) {}
}

export function getAdressListIdentifier(adressList: IAdressList): number | undefined {
  return adressList.id;
}
