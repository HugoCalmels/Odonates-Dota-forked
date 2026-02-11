package com.palladium46.odonatesdota.scrim.model.responses;

public class ScrimDetailsResponse {

    private String errorMessage;
    private boolean canJoinScrim;

    private boolean canCancelScrim;

    private boolean canDeleteScrim;

    private boolean alreadyTagged;

    private boolean hasFinished;

    private boolean hasAlreadySentProposal;


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }

    public boolean isCanJoinScrim() {
        return canJoinScrim;
    }

    public void setCanJoinScrim(boolean canJoinScrim) {
        this.canJoinScrim = canJoinScrim;
    }

    public boolean isCanCancelScrim() {
        return canCancelScrim;
    }

    public void setCanCancelScrim(boolean canCancelScrim) {
        this.canCancelScrim = canCancelScrim;
    }

    public boolean isCanDeleteScrim() {
        return canDeleteScrim;
    }

    public void setCanDeleteScrim(boolean canDeleteScrim) {
        this.canDeleteScrim = canDeleteScrim;
    }

    public boolean isAlreadyTagged() {
        return alreadyTagged;
    }

    public void setAlreadyTagged(boolean alreadyTagged) {
        this.alreadyTagged = alreadyTagged;
    }

    public boolean isHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    public boolean isHasAlreadySentProposal() {
        return hasAlreadySentProposal;
    }

    public void setHasAlreadySentProposal(boolean hasAlreadySentProposal) {
        this.hasAlreadySentProposal = hasAlreadySentProposal;
    }
}
