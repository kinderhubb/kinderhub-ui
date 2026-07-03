const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const SCREENSHOTS_DIR = './screenshots';

if (!fs.existsSync(SCREENSHOTS_DIR)) {
    fs.mkdirSync(SCREENSHOTS_DIR, { recursive: true });
}

async function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function screenshot(page, name) {
    await page.screenshot({ path: path.join(SCREENSHOTS_DIR, `verify-${name}.png`) });
    console.log(`  -> verify-${name}.png`);
}

async function runTests() {
    console.log('Verifying UI fixes...\n');

    const browser = await chromium.launch({
        headless: false,
        args: ['--use-gl=egl', '--enable-webgl', '--ignore-gpu-blocklist']
    });

    const context = await browser.newContext({ viewport: { width: 390, height: 844 } });
    const page = await context.newPage();

    try {
        console.log('1. Loading app and waiting for full render...');
        await page.goto('http://localhost:8080', { waitUntil: 'networkidle', timeout: 60000 });
        await delay(5000); // Wait for Wasm to fully initialize
        await screenshot(page, '01-auth');

        // Click Apple button - it's around y=218 based on earlier tests
        console.log('2. Clicking Apple login button...');
        await page.mouse.click(195, 220);
        await delay(500);
        await page.mouse.click(195, 220); // Double click to ensure
        await delay(4000);
        await screenshot(page, '02-after-apple');

        // If still on auth, try clicking higher
        console.log('3. Checking if navigated...');
        await delay(1000);
        await screenshot(page, '03-current-state');

        // Click Skip (top right) if on onboarding
        console.log('4. Clicking Skip...');
        await page.mouse.click(360, 55);
        await delay(3000);
        await screenshot(page, '04-after-skip');

        // Wait and capture discover
        await delay(2000);
        await screenshot(page, '05-discover');

        // Scroll to see activity cards
        console.log('5. Scrolling to see cards...');
        await page.mouse.move(195, 500);
        await page.mouse.wheel(0, 200);
        await delay(1000);
        await screenshot(page, '06-scrolled');

        // Check desktop auth
        console.log('6. Desktop auth view...');
        await page.goto('http://localhost:8080', { waitUntil: 'networkidle' });
        await delay(4000);
        await page.setViewportSize({ width: 1280, height: 800 });
        await delay(1500);
        await screenshot(page, '07-desktop-auth');

        console.log('\nDone! Check screenshots to verify fixes.');
        await delay(3000);

    } catch (error) {
        console.error('Error:', error.message);
    } finally {
        await browser.close();
    }
}

runTests().catch(console.error);
