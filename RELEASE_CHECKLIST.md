# Release Checklist

Before releasing a new version of the project to public, whatever it is the extension and/or API part which is modified, there are a set of actions to perform or things to verify in order to make the release fully successful and complete. This checklist has to be exhaustive, but is surely not. It has to be updated each time required.

## General

- To verify backward compatibility
    - Are all modifications compatible "as-is" with previous version ?
    - If not, are actions/conceptions/... taken to make previous version working ?
- Is there any conflict in this version's modification with [Todoist Brand Usage Limits](https://developer.todoist.com/sync/v8/#brand-usage) ?
- To update the CHANGE LOG (two changelogs: one for contributor on github ? and another one for public ?)

## Browser Extension

- To verify there is no more TODO in code
- To verify permissions in `manifest.json`
    - Are required permissions asked in file ? In theory yes, otherwise tests must not pass
    - Are all permissions required or are some of them useless ?
- To verify browser compatibility, *a minima* with last version of Chrome and Firefox
- To verify if extension description is up-to-date for public
- To verify if all extension messages are translated
    - `manifest.json`
    - `background.json`
    - all `_locales` file, in order to be sure they have all messages

## Java API

- To verify all Maven compilation warnings and errors
- To verify all Github actions logs, for warning or error
- To build a release version of the API (not a SNAPSHOT one)
- To verify if HTTP requests repository is up to date
- To verify if (API_DEPLOYMENT.md) process is up to date
- To verify if (API_USE_DOCUMENTATION.md) is up to date
- To verify if (PROJECT_LOCAL_SETUP.md) is up to date

## Once deployed

- To verify with existing version if all works well
- To uninstall and reinstall new version and to verify all works well