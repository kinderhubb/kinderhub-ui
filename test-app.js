const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const SCREENSHOTS_DIR = './screenshots';
const APP_URL = 'http://localhost:8080';

// Ensure screenshots directory exists
if (!fs.existsSync(SCREENSHOTS_DIR)) {
    fs.mkdirSync(SCREENSHOTS_DIR, { recursive: true });
}

async function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function takeScreenshot(page, name) {
    const filepath = path.join(SCREENSHOTS_DIR, `${name}.png`);
    await page.screenshot({ path: filepath, fullPage: true });
    console.log(`Screenshot saved: ${filepath}`);
    return filepath;
}

async function runTests() {
    console.log('Starting KinderHub UI Tests...\n');

    const browser = await chromium.launch({
        headless: true,
        args: ['--no-sandbox']
    });

    const context = await browser.newContext({
        viewport: { width: 390, height: 844 }, // iPhone 14 Pro size
        deviceScaleFactor: 2
    });

    const page = await context.newPage();
    const issues = [];

    try {
        // 1. Load the app
        console.log('1. Loading app...');
        await page.goto(APP_URL, { waitUntil: 'networkidle', timeout: 60000 });
        await delay(3000); // Wait for Wasm to initialize
        await takeScreenshot(page, '01-initial-load');

        // Check if app loaded
        const canvas = await page.$('canvas');
        if (!canvas) {
            issues.push('CRITICAL: No canvas element found - app may not have loaded');
        } else {
            console.log('   App canvas detected');
        }

        // 2. Test Auth Screen (if visible)
        console.log('\n2. Testing Auth Screen...');
        await takeScreenshot(page, '02-auth-screen');

        // Try clicking sign in / continue button
        // Since it's a canvas app, we need to click at coordinates
        // Let's click center-bottom area where buttons typically are
        const viewport = page.viewportSize();

        // Click "Continue with Email" or similar button (bottom third of screen)
        console.log('   Attempting to click sign-in button...');
        await page.mouse.click(viewport.width / 2, viewport.height * 0.7);
        await delay(2000);
        await takeScreenshot(page, '03-after-auth-click');

        // 3. Try different screen areas
        console.log('\n3. Exploring different interactions...');

        // Click top area (might be navigation)
        await page.mouse.click(viewport.width / 2, 100);
        await delay(1000);
        await takeScreenshot(page, '04-after-top-click');

        // Click bottom nav area
        await page.mouse.click(viewport.width / 4, viewport.height - 50);
        await delay(1000);
        await takeScreenshot(page, '05-bottom-nav-1');

        await page.mouse.click(viewport.width / 2, viewport.height - 50);
        await delay(1000);
        await takeScreenshot(page, '06-bottom-nav-2');

        await page.mouse.click(viewport.width * 0.75, viewport.height - 50);
        await delay(1000);
        await takeScreenshot(page, '07-bottom-nav-3');

        // 4. Test scrolling
        console.log('\n4. Testing scroll behavior...');
        await page.mouse.wheel(0, 300);
        await delay(500);
        await takeScreenshot(page, '08-after-scroll-down');

        await page.mouse.wheel(0, -300);
        await delay(500);
        await takeScreenshot(page, '09-after-scroll-up');

        // 5. Desktop viewport test
        console.log('\n5. Testing desktop viewport...');
        await page.setViewportSize({ width: 1440, height: 900 });
        await delay(1000);
        await takeScreenshot(page, '10-desktop-view');

        // 6. Tablet viewport test
        console.log('\n6. Testing tablet viewport...');
        await page.setViewportSize({ width: 768, height: 1024 });
        await delay(1000);
        await takeScreenshot(page, '11-tablet-view');

        // Reset to mobile
        await page.setViewportSize({ width: 390, height: 844 });

        console.log('\n=== Test Complete ===');
        console.log(`Screenshots saved to: ${SCREENSHOTS_DIR}/`);
        console.log(`Total issues found: ${issues.length}`);

        if (issues.length > 0) {
            console.log('\nIssues:');
            issues.forEach((issue, i) => console.log(`  ${i + 1}. ${issue}`));
        }

    } catch (error) {
        console.error('Test error:', error.message);
        await takeScreenshot(page, 'error-state');
        issues.push(`ERROR: ${error.message}`);
    } finally {
        await browser.close();
    }

    // Write summary
    const summary = {
        timestamp: new Date().toISOString(),
        screenshotsDir: SCREENSHOTS_DIR,
        issues: issues
    };

    fs.writeFileSync(
        path.join(SCREENSHOTS_DIR, 'test-summary.json'),
        JSON.stringify(summary, null, 2)
    );

    console.log('\nTest summary saved to screenshots/test-summary.json');
}

runTests().catch(console.error);
