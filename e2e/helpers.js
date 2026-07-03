/**
 * E2E Test Helpers for KinderHub UI
 * Utilities for interacting with Compose for Web canvas-based UI
 */

/**
 * Wait for the Wasm/Skiko app to fully initialize
 */
async function waitForAppReady(page, timeout = 10000) {
  // Wait for canvas to be present
  await page.waitForSelector('canvas', { timeout });
  // Additional delay for Wasm initialization
  await page.waitForTimeout(3000);
}

/**
 * Click at specific coordinates on the canvas
 */
async function clickAt(page, x, y, options = {}) {
  const { delay = 100, doubleClick = false } = options;
  await page.mouse.click(x, y);
  if (doubleClick) {
    await page.waitForTimeout(delay);
    await page.mouse.click(x, y);
  }
  await page.waitForTimeout(delay);
}

/**
 * Login via Apple (mock) - clicks the Apple login button
 * On 390x844 viewport: Apple button is at center, around y=240
 */
async function loginWithApple(page) {
  const viewport = page.viewportSize();
  const centerX = viewport.width / 2;
  // Apple button position varies by viewport height
  // On mobile (844 height): around y=240
  // On desktop (800 height): around y=240
  const buttonY = 240;
  await clickAt(page, centerX, buttonY);
  await page.waitForTimeout(3000); // Wait for login to complete
}

/**
 * Skip the onboarding flow by clicking the Skip button
 * The onboarding has 2 steps, we need to skip both
 */
async function skipOnboarding(page) {
  const viewport = page.viewportSize();

  // The onboarding form has a max-width of 500dp and is centered
  // On mobile (<500px): Skip is at viewport.width - 35
  // On desktop (>500px): Skip is at center + 250 - 16 (padding) - 15 (text center)
  //                     = viewport.width/2 + 219

  let skipX;
  if (viewport.width > 600) {
    // Desktop - form is centered with max-width 500
    skipX = (viewport.width / 2) + 219;
  } else {
    // Mobile - form fills width
    skipX = viewport.width - 35;
  }
  const skipY = 58;

  // Click Skip for first onboarding step (may already be on step 2)
  await page.mouse.click(skipX, skipY);
  await page.waitForTimeout(2000);

  // Click Skip for second step
  await page.mouse.click(skipX, skipY);
  await page.waitForTimeout(2000);
}

/**
 * Navigate to a tab (0=Discover, 1=Bookings, 2=Messages, 3=Account)
 * On mobile: bottom navigation bar
 * On desktop: left sidebar
 */
async function navigateToTab(page, tabIndex) {
  const viewport = page.viewportSize();

  let x, y;

  if (viewport.width >= 1000) {
    // Desktop: sidebar on the left
    // Sidebar items are stacked vertically starting around y=100
    // Each item is about 50px tall with padding
    x = 100; // Center of sidebar
    y = 110 + (tabIndex * 48); // Discover=110, Bookings=158, Messages=206, Account=254
  } else {
    // Mobile: bottom navigation bar
    const tabWidth = viewport.width / 4;
    x = tabWidth * tabIndex + tabWidth / 2;
    y = viewport.height - 40;
  }

  await clickAt(page, x, y);
  await page.waitForTimeout(1500);
}

/**
 * Scroll the page vertically
 */
async function scrollPage(page, deltaY) {
  const viewport = page.viewportSize();
  await page.mouse.move(viewport.width / 2, viewport.height / 2);
  await page.mouse.wheel(0, deltaY);
  await page.waitForTimeout(500);
}

/**
 * Take a screenshot and save to e2e-screenshots folder
 */
async function takeSnapshot(page, name) {
  const fs = require('fs');
  const path = require('path');
  const dir = './e2e-screenshots';
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
  await page.screenshot({
    path: path.join(dir, `${name}.png`),
    fullPage: true
  });
}

module.exports = {
  waitForAppReady,
  clickAt,
  loginWithApple,
  skipOnboarding,
  navigateToTab,
  scrollPage,
  takeSnapshot,
};
