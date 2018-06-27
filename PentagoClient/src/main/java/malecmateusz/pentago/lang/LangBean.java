package malecmateusz.pentago.lang;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*
przechowuje aktualne wartości ciągów znaków do użycia w widoku aplikacji
 */
@XStreamAlias("language")
public class LangBean {
    @XStreamAlias("main-menu-game")
    private String mainMenuGameButton = null;
    @XStreamAlias("main-menu-accout")
    private String mainMenuAccountButton = null;
    @XStreamAlias("main-menu-options")
    private String mainMenuOptionsButton = null;
    @XStreamAlias("pop-pl")
    private String popPLBtn = null;
    @XStreamAlias("pop-en")
    private String popENBtn = null;
    @XStreamAlias("pop-lang-label")
    private String popLangLbl = null;
    @XStreamAlias("pop-vol-label")
    private String popVolLbl = null;
    @XStreamAlias("login-user")
    private String loginUsername = null;
    @XStreamAlias("login-pass")
    private String loginPassword = null;
    @XStreamAlias("login-btn")
    private String loginButton = null;
    @XStreamAlias("reg-btn")
    private String registrationButton = null;
    @XStreamAlias("reg-confim")
    private String confimPassword = null;
    @XStreamAlias("login-tab")
    private String loginTabName = null;
    @XStreamAlias("reg-tab")
    private String registrationTabName = null;
    @XStreamAlias("con-err-msg")
    private String connectionErrorMessage = null;
    @XStreamAlias("login-label")
    private String loginLabel = null;
    @XStreamAlias("login-label-error")
    private String loginLabelError = null;
    @XStreamAlias("reg-label")
    private String registrationLabel = null;
    @XStreamAlias("reg-label-pass-error")
    private String registrationLabelPassError = null;
    @XStreamAlias("reg-label-user-exists-error")
    private String registrationLabelUserExsistsError = null;
    @XStreamAlias("reg-label-success")
    private String registrationLabelSuccess = null;
    @XStreamAlias("unable-to-send")
    private String unableToSend = null;
    @XStreamAlias("notyfication")
    private String notyfication = null;
    @XStreamAlias("information")
    private String information = null;
    @XStreamAlias("game-win")
    private String gameWin = null;
    @XStreamAlias("game-lose")
    private String gameLose = null;
    @XStreamAlias("game-leaved")
    private String gameLeaved = null;
    @XStreamAlias("game-your-turn")
    private String gameYourTurn = null;
    @XStreamAlias("msg-prompt")
    private String msgPrompt = null;
    @XStreamAlias("msg-you")
    private String msgYou = null;
    @XStreamAlias("wins")
    private String wins = null;
    @XStreamAlias("loses")
    private String loses = null;
    @XStreamAlias("leaves")
    private String leaves = null;
    @XStreamAlias("hs-title")
    private String highscoreTitle = null;

    public String getHighscoreTitle() {
        return highscoreTitle;
    }

    public void setHighscoreTitle(String highscoreTitle) {
        this.highscoreTitle = highscoreTitle;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLoses() {
        return loses;
    }

    public void setLoses(String loses) {
        this.loses = loses;
    }

    public String getLeaves() {
        return leaves;
    }

    public void setLeaves(String leaves) {
        this.leaves = leaves;
    }

    public String getMsgPrompt() {
        return msgPrompt;
    }

    public void setMsgPrompt(String msgPrompt) {
        this.msgPrompt = msgPrompt;
    }

    public String getMsgYou() {
        return msgYou;
    }

    public void setMsgYou(String msgYou) {
        this.msgYou = msgYou;
    }

    public String getGameWin() {
        return gameWin;
    }

    public void setGameWin(String gameWin) {
        this.gameWin = gameWin;
    }

    public String getGameLose() {
        return gameLose;
    }

    public void setGameLose(String gameLose) {
        this.gameLose = gameLose;
    }

    public String getGameLeaved() {
        return gameLeaved;
    }

    public void setGameLeaved(String gameLeaved) {
        this.gameLeaved = gameLeaved;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getNotyfication() {
        return notyfication;
    }

    public void setNotyfication(String notyfication) {
        this.notyfication = notyfication;
    }

    public String getUnableToSend() {
        return unableToSend;
    }

    public void setUnableToSend(String unableToSend) {
        this.unableToSend = unableToSend;
    }

    public String getRegistrationLabel() {
        return registrationLabel;
    }

    public void setRegistrationLabel(String registrationLabel) {
        this.registrationLabel = registrationLabel;
    }

    public String getRegistrationLabelPassError() {
        return registrationLabelPassError;
    }

    public void setRegistrationLabelPassError(String registrationLabelPassError) {
        this.registrationLabelPassError = registrationLabelPassError;
    }

    public String getRegistrationLabelUserExsistsError() {
        return registrationLabelUserExsistsError;
    }

    public void setRegistrationLabelUserExsistsError(String registrationLabelUserExsistsError) {
        this.registrationLabelUserExsistsError = registrationLabelUserExsistsError;
    }

    public String getRegistrationLabelSuccess() {
        return registrationLabelSuccess;
    }

    public void setRegistrationLabelSuccess(String registrationLabelSuccess) {
        this.registrationLabelSuccess = registrationLabelSuccess;
    }

    public String getLoginLabel() {
        return loginLabel;
    }

    public void setLoginLabel(String loginLabel) {
        this.loginLabel = loginLabel;
    }

    public String getLoginLabelError() {
        return loginLabelError;
    }

    public void setLoginLabelError(String loginLabelError) {
        this.loginLabelError = loginLabelError;
    }



    public String getConnectionErrorMessage() {
        return connectionErrorMessage;
    }

    public void setConnectionErrorMessage(String connectionErrorMessage) {
        this.connectionErrorMessage = connectionErrorMessage;
    }

    public String getLoginTabName() {
        return loginTabName;
    }

    public void setLoginTabName(String loginTabName) {
        this.loginTabName = loginTabName;
    }

    public String getRegistrationTabName() {
        return registrationTabName;
    }

    public void setRegistrationTabName(String registrationTabName) {
        this.registrationTabName = registrationTabName;
    }

    public String getRegistrationButton() {
        return registrationButton;
    }

    public void setRegistrationButton(String registrationButton) {
        this.registrationButton = registrationButton;
    }

    public String getConfimPassword() {
        return confimPassword;
    }

    public void setConfimPassword(String confimPassword) {
        this.confimPassword = confimPassword;
    }



    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(String loginButton) {
        this.loginButton = loginButton;
    }

    public String getMainMenuGameButton() {
        return mainMenuGameButton;
    }

    public void setMainMenuGameButton(String mainMenuGameButton) {
        this.mainMenuGameButton = mainMenuGameButton;
    }

    public String getMainMenuAccountButton() {
        return mainMenuAccountButton;
    }

    public void setMainMenuAccountButton(String mainMenuAccountButton) {
        this.mainMenuAccountButton = mainMenuAccountButton;
    }

    public String getMainMenuOptionsButton() {
        return mainMenuOptionsButton;
    }

    public void setMainMenuOptionsButton(String mainMenuOptionsButton) {
        this.mainMenuOptionsButton = mainMenuOptionsButton;
    }

    public String getPopPLBtn() {
        return popPLBtn;
    }

    public void setPopPLBtn(String popPLBtn) {
        this.popPLBtn = popPLBtn;
    }

    public String getPopENBtn() {
        return popENBtn;
    }

    public void setPopENBtn(String popENBtn) {
        this.popENBtn = popENBtn;
    }

    public String getPopLangLbl() {
        return popLangLbl;
    }

    public void setPopLangLbl(String popLangLbl) {
        this.popLangLbl = popLangLbl;
    }

    public String getPopVolLbl() {
        return popVolLbl;
    }

    public void setPopVolLbl(String popVolLbl) {
        this.popVolLbl = popVolLbl;
    }

    public String getGameYourTurn() {
        return gameYourTurn;
    }

    public void setGameYourTurn(String gameYourTurn) {
        this.gameYourTurn = gameYourTurn;
    }
}
