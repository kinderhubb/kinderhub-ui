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
    console.log('Testing with Apple login...\n');

    const browser = await chromium.launch({
        headless: false,
        args: ['--use-gl=egl', '--enable-webgl', '--ignore-gpu-blocklist']
    });

    const context = await browser.newContext({
        viewport: { width: 390, height: 844 }
    });

    const page = await context.newPage();

    try {
        console.log('1. Loading app...');
        await page.goto('http://localhost:8080', { waitUntil: 'networkidle', timeout: 60000 });
        await delay(4000);
        await screenshot(page, 'apple-01-auth');

        // Click "Continue with Apple" button (top black button)
        console.log('2. Clicking Continue with Apple...');
        await page.mouse.click(195, 218); // Apple button position
        await delay(3000);
        await screenshot(page, 'apple-02-loading');

        await delay(2000);
        await screenshot(page, 'apple-03-after-login');

        // Check if we navigated to a new screen
        console.log('3. Exploring main app...');

        // Try bottom nav icons
        await page.mouse.click(60, 810);  // First nav item
        await delay(1500);
        await screenshot(page, 'apple-04-nav1');

        await page.mouse.click(140, 810); // Second nav item
        await delay(1500);
        await screenshot(page, 'apple-05-nav2');

        await page.mouse.click(250, 810); // Third nav item
        await delay(1500);
        await screenshot(page, 'apple-06-nav3');

        await page.mouse.click(330, 810); // Fourth nav item
        await delay(1500);
        await screenshot(page, 'apple-07-nav4');

        // Go back to first and scroll
        await page.mouse.click(60, 810);
        await delay(1000);
        await screenshot(page, 'apple-08-discover');

        // Scroll down
        await page.mouse.wheel(0, 300);
        await delay(800);
        await screenshot(page, 'apple-09-scrolled');

        // Click on a card
        await page.mouse.click(195, 350);
        await delay(2000);
        await screenshot(page, 'apple-10-detail');

        // Desktop view
        console.log('4. Desktop viewport...');
        await page.setViewportSize({ width: 1280, height: 800 });
        await delay(1500);
        await screenshot(page, 'apple-11-desktop');

        console.log('\nDone! Browser closing in 3 seconds...');
        await delay(3000);

    } catch (error) {
        console.error('Error:', error.message);
    } finally {
        await browser.close();
    }
}

runTests().catch(console.error);
