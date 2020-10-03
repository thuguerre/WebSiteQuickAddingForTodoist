# Release Checklist

Before releasing a new version of the project to public, whatever it is the extension and/or API part which is modified, there are a set of actions to perform or things to verify in order to make the release fully successful and complete. This checklist has to be exhaustive, but is surely not. It has to be updated each time required.

## General

- Verifying backward compatibility
    - Are all modifications compatible "as-is" with previous version ?
    - If not, are actions/conceptions/... taken to make previous version working ?
- Is there any conflict in this version's modification with [Todoist Brand Usage Limits](https://developer.todoist.com/sync/v8/#brand-usage) ?
- Updating the CHANGE LOG (two changelogs: one for contributor on github ? and another one for public ?)

## Browser Extension

- Verifying there is no more TODO in code
- Verifying permissions in `manifest.json`
    - Are required permissions asked in file ? In theory yes, otherwise tests must not pass
    - Are all permissions required or are some of them useless ?
- Verifying browser compatibility, *a minima* with last version of Chrome and Firefox
- Verifying if extension description is up-to-date for public
- Verifying if all extension messages are translated
    - `manifest.json`
    - `background.json`
    - all `_locales` file, in order to be sure they have all messages

## Java API

- Verifying all Maven compilation warnings and errors
- Verifying all Github actions logs, for warning or error
- Building a release version of the API (not a SNAPSHOT one)
- Verifying if HTTP requests repository is up to date
- Verifying if (API_DEPLOYMENT.md) process is up to date
- Verifying if (API_USE_DOCUMENTATION.md) is up to date
- Verifying if (PROJECT_LOCAL_SETUP.md) is up to date

## Once deployed

- Verifying with existing version if all works well
- Uninstalling and reinstalling new version and to verify all works well