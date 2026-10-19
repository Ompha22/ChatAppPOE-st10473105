package ChatAppPOE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/** Unit tests for the Part 2 Message feature. */
public class MessageTest {

    private static final String VALID_RECIPIENT = "+27718693002";
    private static final String INVALID_RECIPIENT = "08575975889";

    @BeforeEach
    public void resetMessageSession() {
        Message.resetSessionForTesting();
    }

    @Test
    public void generatedMessageIdContainsTenDigits() {
        Message message = new Message(0, VALID_RECIPIENT, "Hello there.");

        assertTrue(message.getMessageID().matches("[0-9]{10}"));
        assertTrue(message.checkMessageID());
    }

    @Test
    public void messageIdMustNotExceedTenCharacters() {
        Message valid = new Message("1234567890", 0, VALID_RECIPIENT, "Hello.");
        Message invalid = new Message("12345678901", 0, VALID_RECIPIENT, "Hello.");

        assertTrue(valid.checkMessageID());
        assertFalse(invalid.checkMessageID());
    }

    @Test
    public void messageOf250CharactersIsReadyToSend() {
        Message message = new Message(0, VALID_RECIPIENT, "a".repeat(250));

        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void messageOver250CharactersReportsExcessLength() {
        Message message = new Message(0, VALID_RECIPIENT, "a".repeat(251));

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                message.checkMessageLength()
        );
    }

    @Test
    public void correctlyFormattedRecipientIsAccepted() {
        Message message = new Message(0, VALID_RECIPIENT, "Hello.");

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void incorrectlyFormattedRecipientIsRejected() {
        Message message = new Message(0, INVALID_RECIPIENT, "Hello.");

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain "
                        + "an international code. Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void hashMatchesTheRequiredFirstTestCase() {
        Message message = new Message(
                "0012345678",
                0,
                VALID_RECIPIENT,
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void messageHashesAreCreatedInsideALoop() {
        String[] texts = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?",
            "Hello there."
        };
        String[] expectedHashes = {
            "00:0:HITONIGHT",
            "00:1:HIPAYMENT",
            "00:2:HELLOTHERE"
        };

        for (int index = 0; index < texts.length; index++) {
            Message message = new Message(
                    "0012345678", index, VALID_RECIPIENT, texts[index]
            );
            assertEquals(expectedHashes[index], message.createMessageHash());
        }
    }

    @Test
    public void sendChoiceReturnsRequiredMessageAndIncrementsTotal() {
        Message message = new Message(0, VALID_RECIPIENT, "Hi Mike.");

        assertEquals("Message successfully sent.", message.SentMessage("1"));
        assertEquals("SENT", message.getStatus());
        assertEquals(1, message.returnTotalMessagess());
        assertTrue(message.printMessages().contains("Message: Hi Mike."));
    }

    @Test
    public void discardChoiceReturnsRequiredMessageWithoutCountingAsSent() {
        Message message = new Message(0, INVALID_RECIPIENT, "Hi Keegan.");

        assertEquals("Press 0 to delete the message.", message.SentMessage("2"));
        assertEquals("DISCARDED", message.getStatus());
        assertEquals(0, message.returnTotalMessagess());
    }

    @Test
    public void invalidRecipientCannotBeSent() {
        Message message = new Message(0, INVALID_RECIPIENT, "Hi Keegan.");

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain "
                        + "an international code. Please correct the number and try again.",
                message.SentMessage("1")
        );
        assertFalse(message.isCompleted());
        assertEquals(0, Message.getTotalMessagesSent());
    }

    @Test
    public void storeChoiceWritesAJsonArray(@TempDir Path tempDirectory) throws IOException {
        Path jsonPath = tempDirectory.resolve("messages.json");
        Message.setStorageFilePathForTesting(jsonPath);
        Message message = new Message(
                "0012345678", 0, VALID_RECIPIENT, "Hi \"Mike\"\nsee you soon."
        );

        assertEquals("Message successfully stored.", message.SentMessage("3"));
        assertEquals("STORED", message.getStatus());

        String json = Files.readString(jsonPath, StandardCharsets.UTF_8);
        assertTrue(json.trim().startsWith("["));
        assertTrue(json.trim().endsWith("]"));
        assertTrue(json.contains("\"messageID\": \"0012345678\""));
        assertTrue(json.contains("\\\"Mike\\\""));
        assertTrue(json.contains("\\nsee you soon."));
        assertTrue(json.contains("\"status\": \"STORED\""));
    }

    @Test
    public void sentMessageDetailsUseTheRequiredOrder() {
        Message message = new Message("0012345678", 0, VALID_RECIPIENT, "Hi Mike.");
        message.SentMessage("send");

        String details = message.getFullDetails();
        assertTrue(details.indexOf("Message ID:") < details.indexOf("Message Hash:"));
        assertTrue(details.indexOf("Message Hash:") < details.indexOf("Recipient:"));
        assertTrue(details.indexOf("Recipient:") < details.indexOf("Message:"));
    }
}
