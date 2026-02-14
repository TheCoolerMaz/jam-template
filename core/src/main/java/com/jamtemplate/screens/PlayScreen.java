package com.jamtemplate.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jamtemplate.JamGame;
import com.jamtemplate.ecs.components.PositionComponent;
import com.jamtemplate.ecs.components.VelocityComponent;
import com.jamtemplate.ecs.systems.MovementSystem;
import com.jamtemplate.screens.transitions.FadeTransition;

/**
 * Main gameplay screen.
 * 
 * This is your game! Replace the placeholder with actual gameplay.
 */
public class PlayScreen extends GameScreen {

    private Engine engine;
    private SpriteBatch batch;
    private ShapeRenderer shapes;
    
    private Entity player;
    private float playerX = 640;
    private float playerY = 360;

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        
        // Set up ECS
        engine = new Engine();
        engine.addSystem(new MovementSystem());
        
        // Create player entity
        player = engine.createEntity();
        player.add(new PositionComponent(playerX, playerY));
        player.add(new VelocityComponent(0, 0));
        engine.addEntity(player);
    }

    @Override
    public void render(float delta) {
        // Handle input
        handleInput();
        
        // Update ECS
        engine.update(delta);
        
        // Sync player position from ECS
        PositionComponent pos = player.getComponent(PositionComponent.class);
        playerX = pos.x;
        playerY = pos.y;
        
        // Clear screen
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw placeholder player
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.CYAN);
        shapes.rect(playerX - 25, playerY - 25, 50, 50);
        shapes.end();
        
        // Draw instructions
        batch.begin();
        JamGame.INSTANCE.assets.getFont().draw(batch, 
            "WASD/Arrows to move | ESC to menu", 10, 30);
        batch.end();
    }

    private void handleInput() {
        VelocityComponent vel = player.getComponent(VelocityComponent.class);
        float speed = 200f;
        
        vel.dx = 0;
        vel.dy = 0;
        
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            vel.dy = speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            vel.dy = -speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vel.dx = -speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vel.dx = speed;
        }
        
        // ESC to return to menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            manager.pop(new FadeTransition(0.3f));
        }
    }

    @Override
    public void resize(int width, int height) {
        // Update camera/viewport here if needed
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapes != null) shapes.dispose();
    }
}
