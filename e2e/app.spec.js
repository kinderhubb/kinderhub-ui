const { test, expect } = require('@playwright/test');
const {
  waitForAppReady,
  loginWithApple,
  skipOnboarding,
  navigateToTab,
  scrollPage,
  takeSnapshot,
  clickAt,
} = require('./helpers');

/**
 * KinderHub E2E Tests
 * Tests for Canvas-based Compose for Web UI
 *
 * Coordinate reference (desktop 1280x800):
 * - Sidebar width: ~230px
 * - Main content center: ~640px
 *
 * Coordinate reference (mobile 390x844):
 * - Full width content
 * - Bottom nav height: ~80px
 */

test.describe('App Loading', () => {
  test('should load and display auth screen', async ({ page }) => {
    await page.goto('/');
    await waitForAppReady(page);

    const canvas = await page.$('canvas');
    expect(canvas).toBeTruthy();

    await takeSnapshot(page, 'auth-screen');
  });
});

test.describe('Authentication Flow', () => {
  test('should login with Apple and show onboarding', async ({ page }) => {
    await page.goto('/');
    await waitForAppReady(page);

    await loginWithApple(page);
    await takeSnapshot(page, 'after-apple-login');

    await skipOnboarding(page);
    await takeSnapshot(page, 'after-skip-onboarding');
  });
});

test.describe('Main Navigation', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await skipOnboarding(page);
  });

  test('should display discover screen with activity cards', async ({ page }) => {
    await takeSnapshot(page, 'discover-screen');
    await scrollPage(page, 300);
    await takeSnapshot(page, 'discover-scrolled');
  });

  test('should navigate to bookings tab', async ({ page }) => {
    await navigateToTab(page, 1);
    await takeSnapshot(page, 'bookings-screen');
  });

  test('should navigate to messages tab', async ({ page }) => {
    await navigateToTab(page, 2);
    await takeSnapshot(page, 'messages-screen');
  });

  test('should navigate to account tab', async ({ page }) => {
    await navigateToTab(page, 3);
    await takeSnapshot(page, 'account-screen');
  });
});

test.describe('Activity Detail', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await skipOnboarding(page);
  });

  test('should open activity detail when clicking card', async ({ page }) => {
    const viewport = page.viewportSize();

    // First activity card position
    // Desktop: main content area (after 230px sidebar)
    // Mobile: centered
    const cardX = viewport.width >= 1000 ? 750 : viewport.width / 2;
    const cardY = viewport.width >= 1000 ? 580 : 550;

    await clickAt(page, cardX, cardY);
    await page.waitForTimeout(2000);
    await takeSnapshot(page, 'activity-detail');
  });
});

test.describe('Account Menu', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await skipOnboarding(page);
    await navigateToTab(page, 3);
    await page.waitForTimeout(1000);
  });

  test('should click on child row (Ella)', async ({ page }) => {
    const viewport = page.viewportSize();
    await takeSnapshot(page, 'account-before-click');

    // Ella row position based on Account screen layout:
    // Desktop (1280x800): Sidebar=230px, content starts after
    //   - Profile card: y≈100-180
    //   - Children header: y≈200
    //   - Ella row: y≈230-300 (center at ~265)
    // Mobile (390x844): Full width
    //   - Profile card: y≈100-250
    //   - Children header: y≈280
    //   - Ella row: y≈340-420 (center at ~380)

    let ellaX, ellaY;
    if (viewport.width >= 1000) {
      ellaX = 750; // Center of main content area
      ellaY = 280; // Ella row on desktop
    } else {
      ellaX = viewport.width / 2;
      ellaY = 380; // Ella row on mobile
    }

    await clickAt(page, ellaX, ellaY);
    await page.waitForTimeout(2000);
    await takeSnapshot(page, 'after-child-click');
  });

  test('should click on payment method (Apple Pay)', async ({ page }) => {
    const viewport = page.viewportSize();

    // Payment Methods section is lower on screen
    // Desktop: visible without scroll, around y=500-570
    // Mobile: may need small scroll

    if (viewport.width < 1000) {
      await scrollPage(page, 150);
      await page.waitForTimeout(500);
    }

    await takeSnapshot(page, 'account-payment-section');

    let payX, payY;
    if (viewport.width >= 1000) {
      payX = 750; // Center of main content
      payY = 535; // Apple Pay row on desktop
    } else {
      payX = viewport.width / 2;
      payY = 520; // Apple Pay row on mobile after scroll
    }

    await clickAt(page, payX, payY);
    await page.waitForTimeout(2000);
    await takeSnapshot(page, 'after-payment-click');
  });
});

test.describe('Responsive Layout', () => {
  test('desktop auth should have constrained width', async ({ page, viewport }) => {
    test.skip(viewport.width < 1000, 'Desktop only test');

    await page.goto('/');
    await waitForAppReady(page);
    await takeSnapshot(page, 'desktop-auth');
  });

  test('desktop main app should show sidebar', async ({ page, viewport }) => {
    test.skip(viewport.width < 1000, 'Desktop only test');

    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await skipOnboarding(page);
    await takeSnapshot(page, 'desktop-discover');
  });
});
