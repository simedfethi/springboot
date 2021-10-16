export interface ITripGroupMembers {
  id?: number;
}

export class TripGroupMembers implements ITripGroupMembers {
  constructor(public id?: number) {}
}

export function getTripGroupMembersIdentifier(tripGroupMembers: ITripGroupMembers): number | undefined {
  return tripGroupMembers.id;
}
