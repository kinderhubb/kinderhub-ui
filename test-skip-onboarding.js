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
    await page.screenshot({ path: path.join(SCREENSHOTS_DIR, `${name}.png`) });
    console.log(`  -> ${name}.png`);
}

async function runTests() {
    console.log('Testing main app screens...\n');

    const browser = await chromium.launch({
        headless: false,
        args: ['--use-gl=egl', '--enable-webgl', '--ignore-gpu-blocklist']
    });

    const context = await browser.newContext({ viewport: { width: 390, height: 844 } });
    const page = await context.newPage();

    try {
        console.log('1. Login with Apple...');
        await page.goto('http://localhost:8080', { waitUntil: 'networkidle', timeout: 60000 });
        await delay(4000);

        // Click Apple login
        await page.mouse.click(195, 218);
        await delay(3000);
        await screenshot(page, 'main-01-onboarding');

        // Click "Skip" button (top right)
        console.log('2. Clicking Skip...');
        await page.mouse.click(358, 54);
        await delay(2000);
        await screenshot(page, 'main-02-after-skip');

        // Wait for main screen
        await delay(1500);
        await screenshot(page, 'main-03-discover');

        // Scroll down on discover
        console.log('3. Scrolling discover...');
        await page.mouse.wheel(0, 400);
        await delay(1000);
        await screenshot(page, 'main-04-scrolled');

        // Click on an activity card
        console.log('4. Click activity card...');
        await page.mouse.click(195, 400);
        await delay(2000);
        await screenshot(page, 'main-05-activity-detail');

        // Go back (click back arrow top-left)
        await page.mouse.click(30, 60);
        await delay(1500);

        // Bottom nav - Bookings
        console.log('5. Testing bottom nav...');
        await page.mouse.click(140, 810);
        await delay(1500);
        await screenshot(page, 'main-06-bookings');

        // Bottom nav - Messages
        await page.mouse.click(250, 810);
        await delay(1500);
        await screenshot(page, 'main-07-messages');

        // Bottom nav - Account
        await page.mouse.click(330, 810);
        await delay(1500);
        await screenshot(page, 'main-08-account');

        // Desktop view
        console.log('6. Desktop views...');
        await page.setViewportSize({ width: 1280, height: 800 });
        await delay(1500);
        await screenshot(page, 'main-09-account-desktop');

        // Go to discover on desktop
        await page.mouse.click(100, 760);
        await delay(1500);
        await screenshot(page, 'main-10-discover-desktop');

        console.log('\nDone!');
        await delay(3000);

    } catch (error) {
        console.error('Error:', error.message);
    } finally {
        await browser.close();
    }
}

runTests().catch(console.error);
