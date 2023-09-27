package com.csse3200.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.files.UserSettings.DisplaySettings;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class SettingsMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(SettingsMenuDisplay.class);
  private final GdxGame game;

  private Table rootTable;
  private TextField fpsText;
  private CheckBox fullScreenCheck;
  private CheckBox vsyncCheck;
  private Slider uiScaleSlider;
  private Slider musicVolumeSlider;
  private Slider soundVolumeSlider;
  private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;

  public SettingsMenuDisplay(GdxGame game) {
    super();
    this.game = game;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    Label title = new Label("Settings", skin, "title");
    Table settingsTable = makeSettingsTable();
    Table menuBtns = makeMenuBtns();

    rootTable = new Table();
    rootTable.setFillParent(true);

    rootTable.add(title).expandX().top().padTop(20f);

    rootTable.row().padTop(30f);
    rootTable.add(settingsTable).expandX().expandY();

    rootTable.row();
    rootTable.add(menuBtns).fillX();

    stage.addActor(rootTable);
  }

  private Table makeSettingsTable() {

    UserSettings.Settings settings = UserSettings.get();


    Label fpsLabel = new Label("FPS Cap:", skin);
    fpsText = new TextField(Integer.toString(settings.fps), skin);

    Label fullScreenLabel = new Label("Fullscreen:", skin);
    fullScreenCheck = new CheckBox("", skin);
    fullScreenCheck.setChecked(settings.fullscreen);

    Label vsyncLabel = new Label("VSync:", skin);
    vsyncCheck = new CheckBox("", skin);
    vsyncCheck.setChecked(settings.vsync);

    Label uiScaleLabel = new Label("ui Scale (Unused):", skin);
    uiScaleSlider = new Slider(0.2f, 2f, 0.1f, false, skin);
    uiScaleSlider.setValue(settings.uiScale);
    Label uiScaleValue = new Label(String.format("%.2fx", settings.uiScale), skin);

    Label musicVolumeLabel = new Label("Music Volume:", skin);
    musicVolumeSlider = new Slider(0f, 1f, 0.05f, false, skin);
    musicVolumeSlider.setValue(settings.musicVolume);
    Label musicVolumeValue = new Label(String.format("%.0f%%", settings.musicVolume * 100), skin);

    Label soundVolumeLabel = new Label("Sound Volume:", skin);
    soundVolumeSlider = new Slider(0f, 1f, 0.05f, false, skin);
    soundVolumeSlider.setValue(settings.soundVolume);
    Label soundVolumeValue = new Label(String.format("%.0f%%", settings.soundVolume * 100), skin);

    Label displayModeLabel = new Label("Resolution:", skin);
    displayModeSelect = new SelectBox<>(skin);
    Monitor selectedMonitor = Gdx.graphics.getMonitor();
    displayModeSelect.setItems(getDisplayModes(selectedMonitor));
    displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));


    Table settingsTable = new Table();

    settingsTable.add(fpsLabel).right().padRight(15f);
    settingsTable.add(fpsText).width(100).left();

    settingsTable.row().padTop(10f);
    settingsTable.add(fullScreenLabel).right().padRight(15f);
    settingsTable.add(fullScreenCheck).left();

    settingsTable.row().padTop(10f);
    settingsTable.add(vsyncLabel).right().padRight(15f);
    settingsTable.add(vsyncCheck).left();

    settingsTable.row().padTop(10f);
    Table uiScaleTable = new Table();
    uiScaleTable.add(uiScaleSlider).width(100).left();
    uiScaleTable.add(uiScaleValue).left().padLeft(5f).expandX();

    settingsTable.add(uiScaleLabel).right().padRight(15f);
    settingsTable.add(uiScaleTable).left();

    settingsTable.row().padTop(10f);
    Table musicVolumeTable = new Table();
    musicVolumeTable.add(musicVolumeSlider).width(100).left();
    musicVolumeTable.add(musicVolumeValue).left().padLeft(5f).expandX();

    settingsTable.add(musicVolumeLabel).right().padRight(15f);
    settingsTable.add(musicVolumeTable).left();

    settingsTable.row().padTop(10f);
    Table soundVolumeTable = new Table();
    soundVolumeTable.add(soundVolumeSlider).width(100).left();
    soundVolumeTable.add(soundVolumeValue).left().padLeft(5f).expandX();

    settingsTable.add(soundVolumeLabel).right().padRight(15f);
    settingsTable.add(soundVolumeTable).left();

    settingsTable.row().padTop(10f);
    settingsTable.add(displayModeLabel).right().padRight(15f);
    settingsTable.add(displayModeSelect).left();


    Table table = new Table();

    table.add(settingsTable).expandX().fillX().padBottom(20f).row();


    TextButton okButton = new TextButton("CONTROLS", skin);
    Entity entity = new Entity();
    entity.getEvents().addListener("ok", this::onOK);
    okButton.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("OK button clicked");
                entity.getEvents().trigger("ok");
              }
            });
    table.add(okButton).expandX().center();

    // Events on inputs
    uiScaleSlider.addListener(
            (Event event) -> {
              float value = uiScaleSlider.getValue();
              uiScaleValue.setText(String.format("%.2fx", value));
              return true;
            });

    musicVolumeSlider.addListener(
            (Event event) -> {
              float value = musicVolumeSlider.getValue();
              musicVolumeValue.setText(String.format("%.0f%%", value * 100));
              return true;
            });

    soundVolumeSlider.addListener(
            (Event event) -> {
              float value = soundVolumeSlider.getValue();
              soundVolumeValue.setText(String.format("%.0f%%", value * 100));
              return true;
            });

    return table;
  }

  private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
    DisplayMode active = Gdx.graphics.getDisplayMode();

    for (StringDecorator<DisplayMode> stringMode : modes) {
      DisplayMode mode = stringMode.object;
      if (active.width == mode.width
              && active.height == mode.height
              && active.refreshRate == mode.refreshRate) {
        return stringMode;
      }
    }
    return null;
  }

  private Array<StringDecorator<DisplayMode>> getDisplayModes(Monitor monitor) {
    DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
    Array<StringDecorator<DisplayMode>> arr = new Array<>();

    for (DisplayMode displayMode : displayModes) {
      arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
    }

    return arr;
  }

  private String prettyPrint(DisplayMode displayMode) {
    return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
  }

  private Table makeMenuBtns() {
    TextButton exitBtn = new TextButton("Exit", skin);
    TextButton applyBtn = new TextButton("Apply", skin);


    exitBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
                exitMenu();
              }
            });

    applyBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Apply button clicked");
                applyChanges();
              }
            });

    Table table = new Table();
    table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
    table.add(applyBtn).expandX().right().pad(0f, 0f, 15f, 15f);
    return table;
  }

  private void applyChanges() {
    UserSettings.Settings settings = UserSettings.get();

    Integer fpsVal = parseOrNull(fpsText.getText());
    if (fpsVal != null) {
      settings.fps = fpsVal;
    }
    settings.fullscreen = fullScreenCheck.isChecked();
    settings.uiScale = uiScaleSlider.getValue();
    settings.displayMode = new DisplaySettings(displayModeSelect.getSelected().object);
    settings.vsync = vsyncCheck.isChecked();
    settings.musicVolume = musicVolumeSlider.getValue();
    settings.soundVolume = soundVolumeSlider.getValue();

    UserSettings.set(settings, true);
  }

  private void exitMenu() {
    game.setScreen(ScreenType.MAIN_MENU);
  }

  private void onOK() {
    logger.info("Start game");
    game.setScreen(ScreenType.CONTROL_SCREEN);
  }

  private Integer parseOrNull(String num) {
    try {
      return Integer.parseInt(num, 10);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  protected void draw(SpriteBatch batch)
  {
  }

  @Override
  public void update() {
    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }

  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}