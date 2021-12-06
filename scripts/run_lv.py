import random
import subprocess
import pathlib
import argparse

AGENT_PREFIX = "ag-mt-"
AGENT_CLASS_NAME = "lv.NumberAgent"
ARTIFACT_NAME = "Jade-Average-1.0-SNAPSHOT.jar"

PATH = pathlib.Path(__file__)
ROOT = PATH.parent.parent
BUILD = ROOT / "build"
JAR = BUILD / "libs" / ARTIFACT_NAME


def add_edge(graph, i, j):
    graph[i].add(j)
    graph[j].add(i)


def join(links, i, j):
    p1 = parent(links, i)
    p2 = parent(links, j)
    dominate = min(p1, p2)
    links[p1] = links[p2] = dominate


def parent(links, i) -> int:
    if i == links[i]:
        return i
    links[i] = parent(links, links[i])
    return links[i]


def components(links) -> set:
    return {parent(links, i) for i in range(len(links))}


def components_sets(links) -> dict:
    c = components(links)
    sets = {i: [] for i in c}
    for i in range(len(links)):
        sets[parent(links, i)].append(i)
    return sets


def gen_topology(n: int, m: int):
    links = [i for i in range(n)]
    graph = [set() for _ in range(n)]
    rnd = random.Random()

    # Generate nearly m edges
    for edge in range(m):
        i = rnd.randint(0, n - 1)
        j = rnd.randint(0, n - 1)
        if i != j:
            add_edge(graph, i, j)
            join(links, i, j)

    # Join randomly all components
    while len(components(links)) > 1:
        sets = components_sets(links)
        c = list(sets.keys())
        c1 = random.choice(c)
        c.remove(c1)
        c2 = random.choice(c)
        i = random.choice(sets[c1])
        j = random.choice(sets[c2])
        add_edge(graph, i, j)
        join(links, i, j)

    assert len(components(links)) == 1
    return graph


def get_agents_config(args):
    n = int(args.agents_count)
    m = int(args.agents_links)
    graph = gen_topology(n, m)
    agents = []
    numbers = []
    rnd = random.Random()

    # Generate agents cfg
    for idx in range(n):
        number = rnd.randint(-n * n, n * n)
        edges = graph[idx]
        links = "," + ",".join(map(lambda x: f"{AGENT_PREFIX}{x}", edges)) if len(edges) else ""
        agent = f"{AGENT_PREFIX}{idx}:{AGENT_CLASS_NAME}({number}{links})"
        agents.append(agent)
        numbers.append(number)

    # Eval average of generated numbers
    average = sum(numbers) / float(n)

    return average, numbers, ";".join(agents)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--gui', default=False)
    parser.add_argument('--path', default=JAR)
    parser.add_argument('--agents-count', default=10)
    parser.add_argument('--agents-links', default=20)
    args = parser.parse_args()

    average, numbers, config = get_agents_config(args)
    command = ["java", "-jar", args.path] + (["-gui"] if args.gui else []) + ["-agents", config]
    command_str = " ".join(map(str, command))
    print(f"Expected average {average} for numbers {numbers}")
    print(f"Execute following cmd {command_str}")
    subprocess.call(command)


if __name__ == '__main__':
    main()
