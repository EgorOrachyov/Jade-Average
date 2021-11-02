import subprocess
import pathlib

ARTIFACT_NAME = "Jade-Average-1.0-SNAPSHOT.jar"

PATH = pathlib.Path(__file__)
ROOT = PATH.parent.parent
BUILD = ROOT / "build"
JAR = BUILD / "libs" / ARTIFACT_NAME


def get_agents_config():
    return "A1:NumberAgent"


def main():
    print(f"Execute following jar {JAR}")
    subprocess.call(["java", "-jar", JAR, "-agents", get_agents_config()])


if __name__ == '__main__':
    main()
