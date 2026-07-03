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

async function runTests() {
    console.log('Starting headed browser test...\n');

    // Use headed mode with GPU enabled
    const browser = await chromium.launch({
        headless: false,
        args: [
            '--use-gl=egl',
            '--enable-webgl',
            '--ignore-gpu-blocklist'
        ]
    });

    const context = await browser.newContext({
        viewport: { width: 390, height: 844 }
    });

    const page = await context.newPage();

    try {
        console.log('Loading app...');
        await page.goto(APP_URL, { waitUntil: 'networkidle', timeout: 60000 });

        // Wait longer for Wasm + Skiko to initialize
        console.log('Waiting for app to render...');
        await delay(5000);

        // Take screenshot
        await page.screenshot({
            path: path.join(SCREENSHOTS_DIR, 'headed-mobile.png'),
            type: 'png'
        });
        console.log('Mobile screenshot saved');

        // Desktop view
        await page.setViewportSize({ width: 1440, height: 900 });
        await delay(2000);
        await page.screenshot({
            path: path.join(SCREENSHOTS_DIR, 'headed-desktop.png'),
            type: 'png'
        });
        console.log('Desktop screenshot saved');

        console.log('\nScreenshots saved. Browser will stay open for 10 seconds...');
        await delay(10000);

    } catch (error) {
        console.error('Error:', error.message);
    } finally {
        await browser.close();
    }
}

runTests().catch(console.error);
