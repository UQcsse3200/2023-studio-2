//package com.csse3200.game;
//
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.csse3200.game.ui.DialogueBox;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.lang.reflect.Field;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class DialogueBoxTest {
//
//    @Mock
//    private Stage mockStage;
//
//    @Mock
//    private Skin mockSkin;
//
//    @Mock
//    private TextButton mockOkButton;
//
//    @Mock
//    private TextButton mockNextButton;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//
//        when(mockSkin.get(TextButton.TextButtonStyle.class)).thenReturn(new TextButton.TextButtonStyle());
//        when(mockSkin.get("large", Label.LabelStyle.class)).thenReturn(new Label.LabelStyle());
//
//        when(mockSkin.get(TextButton.class)).thenReturn(mockOkButton).thenReturn(mockNextButton);
//    }
//
//    @Test
//    void testSetDialogueText() throws NoSuchFieldException, IllegalAccessException {
//        DialogueBox dialogueBox = new DialogueBox("Title", "Message", mockSkin);
//        dialogueBox.create();
//        dialogueBox.setDialogueText("New message");
//
//        Field dialogueLabelField = DialogueBox.class.getDeclaredField("dialogueLabel");
//        dialogueLabelField.setAccessible(true);
//        Label dialogueLabel = (Label) dialogueLabelField.get(dialogueBox);
//
//        verify(dialogueLabel).setText("New message");
//    }
//
//    @Test
//    void testShowDialog() {
//        DialogueBox dialogueBox = new DialogueBox("Title", "Message", mockSkin);
//        dialogueBox.create();
//        dialogueBox.showDialog(mockStage);
//        verify(mockStage).addActor(dialogueBox);
//    }
//
//    @Test
//    void testOnOK() {
//        DialogueBox dialogueBox = new DialogueBox("Title", "Message", mockSkin);
//        dialogueBox.create();
//        dialogueBox.onOK();
//        verify(dialogueBox).remove();
//    }
//
//    @Test
//    void testOnInfo() {
//        DialogueBox dialogueBox = new DialogueBox("Title", "Message", mockSkin);
//        dialogueBox.create();
//        dialogueBox.oninfo();
//        verify(dialogueBox).remove();
//    }
//}
