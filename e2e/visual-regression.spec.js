const { test, expect } = require('@playwright/test');
const {
  waitForAppReady,
  loginWithApple,
  skipOnboarding,
  scrollPage,
  takeSnapshot,
  clickAt,
} = require('./helpers');

/**
 * Visual Regression Tests
 * These tests capture screenshots for visual comparison.
 * Run with: npx playwright test e2e/visual-regression.spec.js
 */

test.describe('Visual Regression - Number Formatting', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await skipOnboarding(page);
  });

  test('activity cards should show properly formatted numbers', async ({ page }) => {
    // Wait for discover screen to fully load
    await page.waitForTimeout(2000);
    await takeSnapshot(page, 'vr-discover-numbers');

    // Scroll to see more cards
    await scrollPage(page, 400);
    await page.waitForTimeout(500);
    await takeSnapshot(page, 'vr-discover-numbers-scrolled');
  });

  test('activity detail should show properly formatted numbers', async ({ page }) => {
    const viewport = page.viewportSize();

    // Click on activity card
    await clickAt(page, viewport.width / 2, 550);
    await page.waitForTimeout(2000);
    await takeSnapshot(page, 'vr-detail-numbers');
  });
});

test.describe('Visual Regression - Desktop Layout', () => {
  test('auth form should be centered on desktop', async ({ page, viewport }) => {
    test.skip(viewport.width < 1000, 'Desktop only test');

    await page.goto('/');
    await waitForAppReady(page);
    await takeSnapshot(page, 'vr-desktop-auth-centered');
  });

  test('onboarding form should be centered on desktop', async ({ page, viewport }) => {
    test.skip(viewport.width < 1000, 'Desktop only test');

    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await page.waitForTimeout(2000);
    await takeSnapshot(page, 'vr-desktop-onboarding-centered');
  });

  test('main app should show sidebar on desktop', async ({ page, viewport }) => {
    test.skip(viewport.width < 1000, 'Desktop only test');

    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await skipOnboarding(page);
    await takeSnapshot(page, 'vr-desktop-sidebar');
  });
});

test.describe('Visual Regression - Mobile Layout', () => {
  test('should show bottom navigation on mobile', async ({ page, viewport }) => {
    test.skip(viewport.width > 600, 'Mobile only test');

    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await skipOnboarding(page);
    await takeSnapshot(page, 'vr-mobile-bottom-nav');
  });

  test('activity cards should fit mobile width', async ({ page, viewport }) => {
    test.skip(viewport.width > 600, 'Mobile only test');

    await page.goto('/');
    await waitForAppReady(page);
    await loginWithApple(page);
    await skipOnboarding(page);
    await scrollPage(page, 300);
    await takeSnapshot(page, 'vr-mobile-cards');
  });
});
