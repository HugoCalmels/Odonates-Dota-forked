export interface ScrimDetailsResponse{
  errorMessage: string;
  canJoinScrim: boolean;
  canCancelScrim: boolean;
  canDeleteScrim: boolean;
  alreadyTagged: boolean;
  hasFinished: boolean;
  hasAlreadySentProposal: boolean;
}