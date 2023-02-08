package org.anomek.omegasim.scenarios.partysynergy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import org.anomek.omegasim.OmegaSim;
import org.anomek.omegasim.entities.*;
import org.anomek.omegasim.scenarios.Check;
import org.anomek.omegasim.scenarios.Event;
import org.anomek.omegasim.scenarios.SimScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.anomek.omegasim.helpers.Vectors.distance;
import static org.anomek.omegasim.scenarios.partysynergy.Constants.*;

public class PartySynergyScreen implements SimScreen {

    private final ScenarioRandom random = new ScenarioRandom();

    private final Settings settings;
    private final Textures textures;
    private final OmegaSim sim;

    M m;
    F f;
    Eye eye;
    Fakes fakes;
    Force force;
    Knockback knockback;

    Npc debuf;

    ScenarioPlayer player;
    List<PartyMember> partyMembers;

    SimScreen.Callbacks callbacks;

    public PartySynergyScreen(Textures textures, Settings settings, OmegaSim sim) {
        this.sim = sim;
        this.textures = textures;
        this.settings = settings;
        m = new M(textures, random);
        f = new F(textures, random);
        eye = new Eye(textures, random);
        fakes = new Fakes(textures, random);
        force = new Force(textures, random);
        debuf = new Npc(24 + 10, 32 + 10, 5, random.far ? textures.far : textures.mid);
        debuf.bounds.width = 48;
        debuf.bounds.height = 64;
        debuf.visibility.hide();

        player = new ScenarioPlayer(textures, settings.playerIndx, random);
        player.p.bounds.size(30);
        player.p.bounds.position(500, 500);
        partyMembers = IntStream.range(0, 8)
                .filter(id -> settings.playerIndx != id)
                .mapToObj(id -> new PartyMember(textures, id, random))
                .collect(toList());

        Playstation ps[] = new Playstation[8];
        partyMembers.forEach(member -> {
            Playstation p = new Playstation();
            p.id = random.markers.get(member.id);
            p.texture = textures.playstation().get(p.id);
            member.assign(p);
            ps[member.id] = p;
        });
        Playstation playerPs = new Playstation();
        playerPs.id = random.markers.get(player.indx);
        playerPs.texture = textures.playstation().get(playerPs.id);
        player.assign(playerPs);
        ps[player.indx] = playerPs;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i != j && ps[i].id == ps[j].id) {
                    ps[i].other = ps[j];
                }
            }
        }

        knockback = new Knockback();
        knockback.player = player.p;
        knockback.center = new Vector2(500, 500);
        knockback.affected.add(player.p.bounds);
        knockback.affected.addAll(partyMembers.stream().map(p -> p.npc.bounds).collect(toList()));
        knockback.time = Timing.KNOCKBACK_DURATION;
        knockback.distance = 300;
        knockback.when = TimeUtils.millis() + Timing.KNOCKBACK_RESOLVES;
    }


    //------------------------------

    String resolveAttack() {
        Vector2 pos = player.p.bounds.position;

        Vector2 mpos = m.m.bounds.position;
        boolean inRange = pos.dst2(mpos) < M_ATTACK_AOE_SIZE * M_ATTACK_AOE_SIZE;
        if (inRange != random.altM) {
            return "M Damage";
        }

        Vector2 fpos = f.f.bounds.position;
        if (random.altF) {
            if (distance(fpos, random.attackNorth(), pos) > F_ATTACK_TUNNEL_WIDTH) {
                return "F Damage";
            }
        } else {
            if (distance(fpos, random.attackNorth(), pos) < F_ATTACK_CROSS_WIDTH) {
                return "F Damage";
            }
            if (distance(fpos, random.attackNorth().cpy().rotate90(1), pos) < F_ATTACK_CROSS_WIDTH) {
                return "F Damage";
            }
        }
        return null;
    }

    String resolveEye() {
        Vector2 pos = player.p.bounds.position;
        if (distance(new Vector2(500, 500), random.eyeNorth(), pos) < EYE_ATTACK_WIDTH) {
            return "Eye Damage";
        }
        for (PartyMember member : partyMembers) {
            if (member.npc.bounds.position.dst2(pos) < SPREAD_RADIUS * SPREAD_RADIUS) {
                System.out.println("Close to player");
                return "Spread Damage";
            }
        }
        float tetherSize = player.marker.other.owner.bounds().position.dst(pos);
        if (random.far && tetherSize < 900) {
            return "Far Tether";
        }
        if (!random.far && (tetherSize > 470 || tetherSize < 400)) {
            return "Mid Tether";
        }
        return null;
    }

    String resolveForce() {
        Vector2 pos = player.p.bounds.position;
        for (Npc npc : force.four) {
            if (npc.bounds.position.dst2(pos) < FORCE_AOE * FORCE_AOE) {
                return "M Damage";
            }
        }
        if (force.lastM.bounds.position.dst2(pos) < FORCE_AOE * FORCE_AOE) {
            return "M Damage";
        }

        ArrayList<Vector2> stacks = new ArrayList<>();
        if (player.info.stack) {
            stacks.add(pos);
        }
        for (PartyMember m : partyMembers) {
            if (m.info.stack) {
                stacks.add(m.npc.bounds.position);
            }
        }
        Set<Integer> all = new HashSet<>();
        for (Vector2 stack : stacks) {
            Set<Integer> set = new HashSet<>();
            if (stack.dst2(pos) < 100 * 100) {
                set.add(player.info.index);
            }
            for (PartyMember m : partyMembers) {
                if (stack.dst2(m.npc.bounds.position) < 100 * 100) {
                    set.add(m.id);
                }
            }
            if (set.size() != 4) {
                return "Stack damage";
            }
            all.addAll(set);
        }
        if (all.size() != 8) {
            return "Stack damage";
        }
        return null;
    }

    //------------------------------

    public Arena arena() {
        List<PreparableEntity> list = new ArrayList<>();
        list.addAll(asList(m, f, player, eye, fakes, force, debuf, knockback));
        list.addAll(partyMembers);

        Vector2 dir = new Vector2(0, 308); // north
        boolean circle = true;
        for (Texture texture : textures.waymarks()) {
            Waymark w = new Waymark();
            w.texture = texture;
            w.bounds.size(60);
            w.bounds.position = new Vector2(500, 500).add(dir);
            w.circle = circle;
            circle = !circle;
            dir.rotateDeg(45);
            list.add(w);
        }


        Arena arena = new Arena(list);
        arena.texture = textures.arena;
        return arena;
    }

    public List<Check> checks() {
        return asList(
                new Check(Timing.ATTACK_AOE_DAMAGE, this::resolveAttack),
                new Check(Timing.EYE_AOE_DAMAGE, this::resolveEye),
                new Check(Timing.M_FORCE_AOE_DAMAGE, this::resolveForce)
        );
    }

    public List<Event> events() {
        return asList(
                new Event(0, this::init),
                new Event(Timing.ATTACK_MF_APPEAR, this::attack),
                new Event(Timing.EYE_AOE_DAMAGE, this::stack),
                new Event(Timing.KNOCKBACK_RESOLVES + Timing.KNOCKBACK_DURATION + 1000, this::afterKb)
        );
    }

    private void attack() {
        f.attack();
        m.attack();
        fakes.attack();
    }

    private void init() {
        m.init();
        f.init();
        eye.init();
        force.init();

        partyMembers.forEach(m -> m.init(settings, random));
        player.init(settings, random);

        List<PlayerScenarioInfo> infos = Stream.concat(
                        partyMembers.stream()
                                .map(m -> m.info),
                        Stream.of(player.info)
                )
                .sorted(comparing(p -> p.index))
                .collect(java.util.stream.Collectors.toList());
        infos.forEach(i -> {
            i.linkedTo = infos.get(i.linkedToIndex);
        });

        List<PlayerScenarioInfo> stacks = infos.stream()
                .filter(i -> i.stack)
                .sorted(comparing(p -> p.rank))
                .collect(toList());
        if (stacks.get(0).left == stacks.get(1).left) {
            stacks.get(0).swapForStack = true;
            stacks.get(0).linkedTo.swapForStack = true;
        }

        debuf.visibility.show(Timing.PARTY_SYNERGY_EXECUTION);

    }

    private void stack() {
        partyMembers.forEach(m -> m.stack(random));
    }

    public void afterKb() {
        partyMembers.forEach(PartyMember::runToStack);
    }

    @Override
    public void show() {

    }

    long startTime;
    List<Event> events;
    List<Check> checks;

    boolean failed = false;
    String fail;

    @Override
    public void render(float delta) {
        if (failed) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                callbacks.restart();
                return;
            } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                callbacks.back();
                return;
            }
        }
        long now = TimeUtils.millis();
        if (startTime == 0) {
            startTime = now;
        }
        long duration = now - startTime;
        if (events == null) {
            events = new ArrayList<>(events());
            checks = new ArrayList<>(checks());
        }
        ScreenUtils.clear(.1f, .1f, .1f, 1);

        List<PreparableEntity.PreparedEntity> prepared = arena().prepare();
        if (!failed) {
            prepared.forEach(p -> p.update(now));
        }
        prepared.sort(comparing(PreparableEntity.PreparedEntity::layer));
        prepared.forEach(p -> p.render(sim));

        if (!failed && events.size() > 0 && events.get(0).timestamp < duration) {
            Event event = events.remove(0);
            event.runnable.run();
        }
        if (!failed && checks.size() > 0 && checks.get(0).timestamp < duration) {
            String check = checks.remove(0).test.get();
            if (check != null) {
                failed = true;
                fail = check;
            }
        }
        if (duration > Timing.END_OF_SIM) {
            failed = true;
        }

        if (failed) {
            sim.shape.begin(Filled);
            Color c = sim.shape.getColor().cpy();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sim.shape.setColor(0, 0, 0, .4f);
            sim.shape.rect(0, 0, 1000, 1000);
            sim.shape.setColor(0, 0, 0, .85f);
            sim.shape.rect(250, 400, 500, 200);
            sim.shape.setColor(c);
            sim.shape.end();
            sim.batch.begin();
            sim.font.setColor(Color.WHITE);
            sim.font.draw(sim.batch, fail == null ? "Success" : "Failed to:", 270, 580);
            if (fail != null) {
                sim.font.draw(sim.batch, fail, 270, 550);
            }
            sim.font.draw(sim.batch, "Press space to restart or ESC to go back to settings", 270, 500);
            sim.batch.end();
        }

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose() {
    }

    @Override
    public void registerCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }
}
