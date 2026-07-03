const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const SCREENSHOTS_DIR = './screenshots';
const APP_URL = 'http://localhost:8080';

if (!fs.existsSync(SCREENSHOTS_DIR)) {
    fs.mkdirSync(SCREENSHOTS_DIR, { recursive: true });
}

async function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function screenshot(page, name) {
    await page.screenshot({ path: path.join(SCREENSHOTS_DIR, `${name}.png`) });
    console.log(`  -> ${name}.png`);
}

async function runTests() {
    console.log('Testing full app flow...\n');

    const browser = await chromium.launch({
        headless: false,
        args: ['--use-gl=egl', '--enable-webgl', '--ignore-gpu-blocklist']
    });

    const context = await browser.newContext({
        viewport: { width: 390, height: 844 }
    });

    const page = await context.newPage();

    try {
        // Load app
        console.log('1. Loading app...');
        await page.goto(APP_URL, { waitUntil: 'networkidle', timeout: 60000 });
        await delay(4000);
        await screenshot(page, 'flow-01-auth');

        // Click Log in button (bottom area of screen)
        console.log('2. Clicking Log in...');
        await page.mouse.click(195, 568); // Log in button position
        await delay(2000);
        await screenshot(page, 'flow-02-after-login');

        // Wait for potential navigation
        await delay(2000);
        await screenshot(page, 'flow-03-main-screen');

        // Try bottom nav - left icon (Discover)
        console.log('3. Testing bottom navigation...');
        await page.mouse.click(60, 800);
        await delay(1500);
        await screenshot(page, 'flow-04-discover');

        // Bottom nav - second icon (Bookings)
        await page.mouse.click(150, 800);
        await delay(1500);
        await screenshot(page, 'flow-05-bookings');

        // Bottom nav - third icon (Messages)
        await page.mouse.click(240, 800);
        await delay(1500);
        await screenshot(page, 'flow-06-messages');

        // Bottom nav - fourth icon (Account)
        await page.mouse.click(330, 800);
        await delay(1500);
        await screenshot(page, 'flow-07-account');

        // Go back to discover and scroll
        console.log('4. Testing scroll on Discover...');
        await page.mouse.click(60, 800);
        await delay(1000);
        await page.mouse.wheel(0, 400);
        await delay(500);
        await screenshot(page, 'flow-08-discover-scrolled');

        // Try clicking on an activity card (middle of screen)
        console.log('5. Clicking activity card...');
        await page.mouse.click(195, 400);
        await delay(2000);
        await screenshot(page, 'flow-09-activity-detail');

        // Desktop test
        console.log('6. Desktop view test...');
        await page.setViewportSize({ width: 1440, height: 900 });
        await delay(1500);
        await screenshot(page, 'flow-10-desktop');

        // Tablet test
        console.log('7. Tablet view test...');
        await page.setViewportSize({ width: 768, height: 1024 });
        await delay(1500);
        await screenshot(page, 'flow-11-tablet');

        console.log('\n✓ All screenshots captured!');
        console.log('Keeping browser open for 5 seconds...');
        await delay(5000);

    } catch (error) {
        console.error('Error:', error.message);
        await screenshot(page, 'flow-error');
    } finally {
        await browser.close();
    }
}

runTests().catch(console.error);
