package nl.kolossus.gymlust;

/**
 * Created by ericdehaan on 26-07-17.
 */

class AppUser {
    private static final AppUser ourInstance = new AppUser();

    static AppUser getInstance() {
        return ourInstance;
    }

    boolean isBeamReferee = false;
    boolean isVaultReferee = false;
    boolean isPonyReferee = false;
    boolean isFloorReferee = false;

    private AppUser() {
    }

    boolean isReferee() {
        return isBeamReferee || isFloorReferee || isPonyReferee || isVaultReferee;
    }

    public void logOffReferee() {
        isBeamReferee = false;
        isPonyReferee = false;
        isFloorReferee = false;
        isVaultReferee = false;
    }
}
