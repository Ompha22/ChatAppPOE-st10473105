package ChatAppPOE;

import ChatAppPOE.Login;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {


    private Login createValidAccount() {

        return new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976"
        );
    }


    // ---------------------------------------------------------
    // USERNAME TESTING
    // ---------------------------------------------------------

    @Test
    public void usernameShouldBeValid() {

        Login account = createValidAccount();

        assertTrue(account.checkUserName());
    }


    @Test
    public void usernameShouldBeInvalid() {

        Login account = new Login(
                "Kyle",
                "Smith",
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        assertFalse(account.checkUserName());
    }


    @Test
    public void validUsernameShouldReturnCorrectMessage() {

        Login account = createValidAccount();

        assertEquals(
                "Username successfully captured.",
                account.usernameMessage()
        );
    }


    @Test
    public void invalidUsernameShouldReturnCorrectMessage() {

        Login account = new Login(
                "Kyle",
                "Smith",
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        String expected =
                "Username is not correctly formatted; "
                + "please ensure that your username contains "
                + "an underscore and is no more than five "
                + "characters in length.";

        assertEquals(
                expected,
                account.usernameMessage()
        );
    }


    // ---------------------------------------------------------
    // PASSWORD TESTING
    // ---------------------------------------------------------

    @Test
    public void complexPasswordShouldPass() {

        Login account = createValidAccount();

        assertTrue(
                account.checkPasswordComplexity()
        );
    }


    @Test
    public void weakPasswordShouldFail() {

        Login account = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "password",
                "+27838968976"
        );

        assertFalse(
                account.checkPasswordComplexity()
        );
    }


    @Test
    public void validPasswordShouldReturnCorrectMessage() {

        Login account = createValidAccount();

        assertEquals(
                "Password successfully captured.",
                account.passwordMessage()
        );
    }


    @Test
    public void invalidPasswordShouldReturnCorrectMessage() {

        Login account = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "password",
                "+27838968976"
        );

        String expected =
                "Password is not correctly formatted; "
                + "please ensure that the password contains "
                + "at least eight characters, a capital letter, "
                + "a number, and a special character.";

        assertEquals(
                expected,
                account.passwordMessage()
        );
    }


    // ---------------------------------------------------------
    // CELLPHONE TESTING
    // ---------------------------------------------------------

    @Test
    public void correctCellNumberShouldPass() {

        Login account = createValidAccount();

        assertTrue(
                account.checkCellPhoneNumber()
        );
    }


    @Test
    public void incorrectCellNumberShouldFail() {

        Login account = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553"
        );

        assertFalse(
                account.checkCellPhoneNumber()
        );
    }


    @Test
    public void validCellNumberShouldReturnCorrectMessage() {

        Login account = createValidAccount();

        assertEquals(
                "Cell number successfully captured.",
                account.cellPhoneMessage()
        );
    }


    @Test
    public void invalidCellNumberShouldReturnCorrectMessage() {

        Login account = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553"
        );

        String expected =
                "Cell number is incorrectly formatted or does "
                + "not contain an international code; please "
                + "correct the number and try again.";

        assertEquals(
                expected,
                account.cellPhoneMessage()
        );
    }


    // ---------------------------------------------------------
    // REGISTRATION TESTING
    // ---------------------------------------------------------

    @Test
    public void validAccountShouldRegister() {

        Login account = createValidAccount();

        assertEquals(
                "User successfully registered.",
                account.registerUser()
        );
    }


    @Test
    public void invalidUsernameShouldStopRegistration() {

        Login account = new Login(
                "Kyle",
                "Smith",
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976"
        );

        String expected =
                "Username is not correctly formatted; "
                + "please ensure that your username contains "
                + "an underscore and is no more than five "
                + "characters in length.";

        assertEquals(
                expected,
                account.registerUser()
        );
    }


    @Test
    public void weakPasswordShouldStopRegistration() {

        Login account = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "password",
                "+27838968976"
        );

        String expected =
                "Password is not correctly formatted; "
                + "please ensure that the password contains "
                + "at least eight characters, a capital letter, "
                + "a number, and a special character.";

        assertEquals(
                expected,
                account.registerUser()
        );
    }


    // ---------------------------------------------------------
    // LOGIN TESTING
    // ---------------------------------------------------------

    @Test
    public void correctCredentialsShouldLogin() {

        Login account = createValidAccount();

        boolean result =
                account.loginUser(
                        "kyl_1",
                        "Ch&&sec@ke99!"
                );

        assertTrue(result);
    }


    @Test
    public void incorrectCredentialsShouldNotLogin() {

        Login account = createValidAccount();

        boolean result =
                account.loginUser(
                        "wrong",
                        "Wrong123!"
                );

        assertFalse(result);
    }


    @Test
    public void successfulLoginShouldDisplayWelcomeMessage() {

        Login account = createValidAccount();

        String result =
                account.returnLoginStatus(
                        "kyl_1",
                        "Ch&&sec@ke99!"
                );

        assertEquals(
                "Welcome Kyle, Smith it is great to see you again.",
                result
        );
    }


    @Test
    public void failedLoginShouldDisplayErrorMessage() {

        Login account = createValidAccount();

        String result =
                account.returnLoginStatus(
                        "wrong",
                        "Wrong123!"
                );

        assertEquals(
                "Username or password incorrect, please try again.",
                result
        );
    }
}