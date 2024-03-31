import { defineConfig } from 'cypress'
import * as registerCodeCoverageTasks from '@cypress/code-coverage/task';

export default defineConfig({
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  supportFolder: 'cypress/support',
  video: false,
  e2e: {
    // We've imported your old cypress plugins here.
    // You may want to clean this up later by importing these.
    setupNodeEvents(on, config) {
      registerCodeCoverageTasks(on, config)
      require('./cypress/plugins/index.ts').default(on, config)
      return config
    },
    baseUrl: 'http://localhost:4200',
  },
})
