package me.hysong.dev.site.modules.docsign;

import lombok.Getter;

@Getter
public class SignatureReport {

    private final String signVersion;
    private final String signerName;
    private final String signerEmail;
    private final long signedAt;
    private final long validThrough;
    private final String currentlyValid;
    private final String miniNote;
    private final String unsignedContentExpectedSHA;
    private final String signedVersionCompatibility;
    private final String mailMatches;
    private final String timeValid;
    private final String unsignedMatches;
    private final String validity;
    private final String signMatch;
    private final String uniquePair;

    public SignatureReport(String signVersion, String signerName, String signerEmail, long signedAt, long validThrough, String currentlyValid, String miniNote, String unsignedContentExpectedSHA, String signedVersionCompatibility, String mailMatches, String timeValid, String unsignedMatches, String validity, String signMatch, String uniquePair) {
        this.signVersion = signVersion;
        this.signerName = signerName;
        this.signerEmail = signerEmail;
        this.signedAt = signedAt;
        this.validThrough = validThrough;
        this.currentlyValid = currentlyValid;
        this.miniNote = miniNote;
        this.unsignedContentExpectedSHA = unsignedContentExpectedSHA;
        this.signedVersionCompatibility = signedVersionCompatibility;
        this.mailMatches = mailMatches;
        this.timeValid = timeValid;
        this.unsignedMatches = unsignedMatches;
        this.validity = validity;
        this.signMatch = signMatch;
        this.uniquePair = uniquePair;
    }
}
