package com.example.addon.modules.hud;



import com.example.addon.Addon;
import com.example.addon.Utils.Formatter;
import meteordevelopment.meteorclient.renderer.GL;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.hud.*;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Calendar;

public class LogoHud extends HudElement {
    public static final HudElementInfo<LogoHud> INFO = new HudElementInfo<>(Addon.HUD_GROUP, "logo-hud", "Display the Banana+ logo.", LogoHud::new);

    public enum Logo {
        Default,
        British,
        Festive,
        Spooky,
        Mexican,
        Irish,
        Patriot,
        Pride,
        Swedish
    }

    public enum Default {
        Default,
        Plus2,
        Plus3,
        Circle
    }

    public enum Halloween {
        Default,
        Ghosts,
        Pumpkin
    }


    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    // General
    private final Setting<Logo> logo = sgGeneral.add(new EnumSetting.Builder<Logo>()
        .name("logo-image")
        .description("Which image to display as the logo.")
        .defaultValue(Logo.Default)
        .build()
    );

    private final Setting<Boolean> events = sgGeneral.add(new BoolSetting.Builder()
        .name("events")
        .description("Use seasonal edits of the logo for holidays!")
        .defaultValue(true)
        .build()
    );

    private final Setting<Default> normal = sgGeneral.add(new EnumSetting.Builder<Default>()
        .name("default-mode")
        .description("Which image to display as the logo.")
        .defaultValue(Default.Default)
        .build()
    );

    private final Setting<Halloween> halloween = sgGeneral.add(new EnumSetting.Builder<Halloween>()
        .name("spooky-mode")
        .description("Which image to display for the spooky month.")
        .defaultValue(Halloween.Default)
        .visible (() -> events.get() || logo.get() == Logo.Spooky)
        .build()
    );

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("How large the logo should be.")
        .defaultValue(2)
        .min(0.1)
        .sliderRange(0,4)
        .build()
    );


    public LogoHud() {
        super(INFO);
    }


    // Normal Logos
    private static final Identifier LOGO = new Identifier("assets", "logo1.png");
    private static final Identifier LOGO2 = new Identifier("assets", "logo2.png");
    private static final Identifier LOGO3 = new Identifier("assets", "logo3.png");
    private static final Identifier CIRCLE = new Identifier("assets", "circle.png");


    // Seasonal Logos
    private static final Identifier BRITISH = new Identifier("assets", "british.png");
    private static final Identifier CHRISTMAS = new Identifier("assets", "christmas.png");
    private static final Identifier HALLOWEEN = new Identifier("assets", "halloween1.png");
    private static final Identifier HALLOWEEN2 = new Identifier("assets", "halloween2.png");
    private static final Identifier HALLOWEEN3 = new Identifier("assets", "halloween3.png");
    private static final Identifier MEXICO = new Identifier("assets", "mexican.png");
    private static final Identifier PATRICK = new Identifier("assets", "patrick.png");
    private static final Identifier PATRIOT = new Identifier("assets", "patriot.png");
    private static final Identifier PRIDE = new Identifier("assets", "pride.png");
    private static final Identifier SWEDEN = new Identifier("assets", "swedish.png");

    public Identifier getNormal() {
        if (normal.get() == Default.Default) return LOGO;
        if (normal.get() == Default.Plus2) return LOGO2;
        if (normal.get() == Default.Plus3) return LOGO3;
        return CIRCLE;
    }

    public Identifier getSpooky() {
        if (halloween.get() == Halloween.Default) return HALLOWEEN;
        if (halloween.get() == Halloween.Pumpkin) return HALLOWEEN2;
        return HALLOWEEN3;
    }

    public Identifier getDefault() {
        if (logo.get() == Logo.British) return BRITISH;
        if (logo.get() == Logo.Festive) return CHRISTMAS;
        if (logo.get() == Logo.Spooky) return getSpooky();
        if (logo.get() == Logo.Mexican) return MEXICO;
        if (logo.get() == Logo.Irish) return PATRICK;
        if (logo.get() == Logo.Patriot) return PATRIOT;
        if (logo.get() == Logo.Pride) return PRIDE;
        if (logo.get() == Logo.Swedish) return SWEDEN;
        return getNormal();
    }

    @Override
    public void tick(HudRenderer renderer) {
        box.setSize(90 * scale.get(), 90 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        if (!Utils.canUpdate()) return;

        double x = this.x;
        double y = this.y;
        int w = this.getWidth();
        int h = this.getHeight();
        Color color = new Color (255, 255, 255);

        if (events.get()) {
            // June
            if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE && Calendar.getInstance().get(Calendar.DATE) == 6) GL.bindTexture(SWEDEN); // Sweden Day
            else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE && Calendar.getInstance().get(Calendar.DATE) == 12) GL.bindTexture(BRITISH); // British National Day
            else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE) GL.bindTexture(PRIDE); // Pride Month
                // July
            else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JULY && Calendar.getInstance().get(Calendar.DATE) == 4) GL.bindTexture(PATRIOT); // Independence Day
                // March
            else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.MARCH && Calendar.getInstance().get(Calendar.DATE) == 17) GL.bindTexture(PATRICK); // St. Patrick's Day
                // September
            else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.SEPTEMBER && Calendar.getInstance().get(Calendar.DATE) == 16) GL.bindTexture(MEXICO); // Mexican Independence
                // October
            else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER) GL.bindTexture(getSpooky()); // Spooky Month
                // December
            else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.DATE) == 25) GL.bindTexture(CHRISTMAS); // Christmas
                // Default if it's not a holiday
            else GL.bindTexture(getDefault());
        }

        if (!events.get()) GL.bindTexture(getDefault());

        Renderer2D.TEXTURE.begin();
        Renderer2D.TEXTURE.texQuad(x, y, w, h, color);
        Renderer2D.TEXTURE.render(null);
    }

    public static class Killfeed extends HudElement {

        public static final HudElementInfo<Killfeed> INFO = new HudElementInfo<>(Addon.HUD_GROUP, "killfeed", "Display a list of your kills", Killfeed::new);

        private final ArrayList<String> feed = new ArrayList<>();

        private void updateFeed() {
            feed.clear();
            if (Formatter.hasKillFeed()) feed.addAll(Formatter.getKillFeed());
        }

        public Killfeed() {
            super(INFO);
        }

        @Override
        public void tick(HudRenderer renderer) {
            updateFeed();
            double width = 0;
            double height = 0;
            int i = 0;
            if (feed.isEmpty()) {
                String t = "Killfeed";
                width = Math.max(width, renderer.textWidth(t));
                height += renderer.textHeight();
            } else {
                width = Math.max(width, renderer.textWidth("Killfeed"));
                height += renderer.textHeight();
                for (String bind : feed) {
                    width = Math.max(width, renderer.textWidth(bind));
                    height += renderer.textHeight();
                    if (i > 0) height += 2;
                    i++;
                }
            }
            setSize(width, height);
        }

        @Override
        public void render(HudRenderer renderer) {
            Hud hud = Hud.get();
            var color = hud.textColors.get().get(0);
            updateFeed();
            double x = getX();
            double y = getY();
            if (isInEditor()) {
                renderer.text("Killfeed", x, y, color, false);
                return;
            }
            int i = 0;
            if (feed.isEmpty()) {
                String t = "Killfeed";
                renderer.text(t, x + alignX(renderer.textWidth(t), Alignment.Auto), y, color, false);
            } else {
                renderer.text("Killfeed", x + alignX(renderer.textWidth("Killfeed"), Alignment.Auto), y, color, false);
                y += renderer.textHeight();
                for (String bind : feed) {
                    renderer.text(bind, x + alignX(renderer.textWidth(bind), Alignment.Auto), y, color, false);
                    y += renderer.textHeight();
                    if (i > 0) y += 2;
                    i++;
                }
            }
        }

    }
}
