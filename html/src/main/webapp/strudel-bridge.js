// Strudel Bridge v7 - Using @strudel/web (the simple API)
// This provides initStrudel() and pattern.play() / hush()

let ready = false;
let pendingCode = null;
let initialized = false;

async function initStrudel() {
    // Wait for strudel to be available (loaded via script tag)
    let attempts = 0;
    while (typeof initStrudel === 'undefined' && typeof window.initStrudel === 'undefined' && attempts < 50) {
        await new Promise(r => setTimeout(r, 100));
        attempts++;
    }
    
    // Check what's available from @strudel/web
    console.log('[Strudel] Checking @strudel/web globals...');
    console.log('  note:', typeof window.note);
    console.log('  stack:', typeof window.stack);
    console.log('  s:', typeof window.s);
    console.log('  hush:', typeof window.hush);
    console.log('  silence:', typeof window.silence);
    
    if (typeof window.note === 'function') {
        ready = true;
        console.log('[Strudel] @strudel/web ready!');
        
        if (pendingCode) {
            window.Strudel.evaluate(pendingCode);
            pendingCode = null;
        }
    } else {
        console.error('[Strudel] @strudel/web functions not found');
    }
}

// Wait for script to load then init
setTimeout(initStrudel, 1000);

window.Strudel = {
    evaluate: function(code) {
        if (!ready) {
            console.log('[Strudel] Not ready, queuing...');
            pendingCode = code;
            return;
        }
        
        try {
            console.log('[Strudel] Evaluating pattern...');
            
            // Store the current code so we can replay with new volume
            window._currentPatternCode = code.trim();
            
            // Get current volume (default 0.7)
            const vol = window._strudelVolume !== undefined ? window._strudelVolume : 0.7;
            
            // The code contains strudel DSL - we can eval it directly
            // since @strudel/web exposes all functions globally
            // Wrap pattern with .gain() to apply volume
            const pattern = eval(code.trim());
            
            // If it returns a pattern with .play(), apply gain and play
            if (pattern && typeof pattern.play === 'function') {
                // Apply volume via .gain() modifier
                const withVolume = pattern.gain(vol);
                withVolume.play();
                console.log('[Strudel] Playing with volume:', vol);
            } else {
                console.log('[Strudel] Pattern evaluated (no .play method)');
            }
        } catch (e) {
            console.error('[Strudel] Eval error:', e);
        }
    },
    
    hush: function() {
        if (typeof window.hush === 'function') {
            window.hush();
            console.log('[Strudel] Hushed');
        }
    },
    
    setVolume: function(volume) {
        window._strudelVolume = Math.max(0, Math.min(1, volume));
        console.log('[Strudel] Volume set to:', window._strudelVolume);
        
        // Re-play current pattern with new volume if one is playing
        if (window._currentPatternCode && ready) {
            this.evaluate(window._currentPatternCode);
        }
    },
    
    setParam: function(name, value) {
        console.log('[Strudel] setParam:', name, value);
    }
};

console.log('[Strudel] Bridge v7 (@strudel/web) loading...');
