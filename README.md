This is a tool that can export Excel according to excel template.
Depends on excel template format.

- This tool is used to generate ARS and OMeS example from INES adaptation package.
- The INES adaptation package could be from SVN or local file system.

Usage:
1. Provide configuration file with json format, see examples in AdaptationCollector/config folder.
2. Go to AdaptationCollector folder, run "mvn clean install".
    Note: You need install maven before run this command.
3. Run command:
    .\AdaptationCollector.bat <path_to_config.json>
