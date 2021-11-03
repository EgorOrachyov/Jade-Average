# Jade-Average

Average value of random numbers generated in a multi-agent environment.
- [Project page](https://github.com/EgorOrachyov/Jade-Average)
- [License](https://github.com/EgorOrachyov/Jade-Average/blob/main/LICENSE)
- [Jade tutorial](https://github.com/EgorOrachyov/Jade-Average/blob/main/docs/jade-tutorial.pdf)

### Algorithm
Algorithm name is `BFS from chosen leader`.
The idea of the algorithm is to choose by voting a leader based on
agents ids and the initiate bfs from leader to collect sums of children
and get the result sum in leader to send it to the `center`.

### Metrics
| Memory | Messages in network | Operations count | Time | Number of links | Messages to center |
|:---    |:---                 |:---              |:---  |:---             |:---                |
| O(n)   | O(m * n)            | O(n)             | O(n) | any             | O(1)               | 

> n - number of agents in the network.  
> m - number of edges in agents network.  
> O(*) - big o notation.

### Technical details

#### Algorithm assumptions
- Agents network topology is arbitrary
- Connections between agents are bi-directed
- Topology can't change in time of algorithm
- Agents count is fixed and limited by `n`

#### Agent stored data
- `Id` (int) unique id of the agent
- `N` (int) total number of agents in the network
- `Number` (int) random generated number of the agent
- `State` (int) current state of the agent
- `Leader` (int) id of the chosen leader agent

#### Agent input data
- `Id` in range `[0, N)`
- `N` total number of agents in the network
- `Number` random number
- `Links` known neighbors of the agent (for communication)

#### Algorithm steps:
- **Voting**: 
  - Each agent sends to its neighbors its id and receives ids from his neighbors and saves `leader = min(self.id, ids)`. 
  - This process is repeated for `N` rounds. 
  - After `N` rounds it is guaranteed that leader id is known for each agent. 
  - As the result, each agent knows leader id.
- **Bfs**: 
  - Leader initiates bfs. 
  - He sends request to know `(sum, n)` of its children.
  - If agent receives request first time from some agent `k`, then he is child of `k`.
  - Agent marks himself as visited, initializes self `(sum = self.Number, k = 1)` and tries to send request further to his children.
  - If all children visited, then agent returns `(self.Number, 1)`.
  - Else agent waits for children, he successfully sent request.
  - For each response from child of kind `(sum, k)`, agent updates his sum `(self.sum + sum, self.k + k)`.
  - When leader receives feedback from all his children, he computes final `(sum + self.Number, k + 1)`.
- **Notify**:
  - Leader node computes `average = sum / k`.
  - Leader sends `average` to the center.

## How to get and run

### Requirements
- Java SDK
- Gradle
- Python 3.8+
- *Optional: Intellij IDEA*

### Get source code
The following code snipped show how to get repository source code.
Execute it in the folder where you want to locate the project.

```shell
$ git clone https://github.com/EgorOrachyov/Jade-Average.git
```

Import `Jade-Average` as IntelliJ project.

### Build executable JAR
Run `build.gradle` `fatJar` task to build executable jar file.
Main class inside jar is the following: `jade.Boot`.

### Run application
The following code snipped show how to generate agents network
and run `jade` simulation. This script must be run from the root directory of the project.

```shell
python ./scripts/run.py --gui=True --agents-count=`n` --agents-links=`m`
```

Where scripts allows providing following options:
- `--gui` run jade gui window
- `--agents-count` number of agents to generate
- `--agents-links` approximate number of links between agents to generate
- `--path` absolute path to jar to execute, default is `${project.root}/build/libs/Jade-Average-1.0-SNAPSHOT.jar`

## Details about run
Script before actual `jade` start outputs to the console all the information
about agents, network, expected average and list of generated numbers.
Look at the following run example, where we spawn network with 4 agents
and nearly 4 links between them (note, required links for graph connectivity are added automatically).

```shell
python ./scripts/run.py --agents-count=4 --agents-links=4
```

Script possible output:

```text
Expected average -2.5 for numbers [-7, -8, -2, 7]
Execute following cmd java -jar .\Jade-Average\build\libs\Jade-Average-1.0-SNAPSHOT.jar -gui -agents 
    'ag-mt-0:NumberAgent(0,4,-7,ag-mt-1,ag-mt-2);
     ag-mt-1:NumberAgent(1,4,-8,ag-mt-0);
     ag-mt-2:NumberAgent(2,4,-2,ag-mt-0,ag-mt-3);
     ag-mt-3:NumberAgent(3,4,7,ag-mt-2)'
```

Jade possible output and agents logs:

```text
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-1 vote round 0
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-3 vote round 0
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-2 vote round 0
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-0 vote round 0
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-1 vote round 1
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-0 vote round 1
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-2 vote round 1
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-3 vote round 1
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-0 vote round 2
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-1 vote round 2
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-2 vote round 2
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-3 vote round 2
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-0 vote round 3
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-1 vote round 3
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-2 vote round 3
Nov 03, 2021 12:27:20 PM ChooseLeaderBehaviour action
INFO: ag-mt-3 vote round 3
Nov 03, 2021 12:27:20 PM TransitionBehaviour action
INFO: ag-mt-3 I am child, go to bfs
Nov 03, 2021 12:27:20 PM TransitionBehaviour action
INFO: ag-mt-1 I am child, go to bfs
Nov 03, 2021 12:27:20 PM TransitionBehaviour action
INFO: ag-mt-0 I am leader, go to bfs
Nov 03, 2021 12:27:20 PM TransitionBehaviour action
INFO: ag-mt-2 I am child, go to bfs
Nov 03, 2021 12:27:20 PM LeaderBfsBehaviour action
INFO: send visit to ag-mt-2
Nov 03, 2021 12:27:20 PM LeaderBfsBehaviour action
INFO: send visit to ag-mt-1
Nov 03, 2021 12:27:20 PM ChildBfsBehaviour action
INFO: ag-mt-2 get visit (yet not visited, accept)
Nov 03, 2021 12:27:20 PM ChildBfsBehaviour action
INFO: ag-mt-1 get visit (yet not visited, accept)
Nov 03, 2021 12:27:20 PM ChildBfsBehaviour action
INFO: ag-mt-2 send visit to ag-mt-3
Nov 03, 2021 12:27:20 PM ChildBfsBehaviour action
INFO: ag-mt-1 send response
Nov 03, 2021 12:27:20 PM ChildBfsBehaviour action
INFO: ag-mt-3 get visit (yet not visited, accept)
Nov 03, 2021 12:27:20 PM LeaderBfsBehaviour action
INFO: get response
Nov 03, 2021 12:27:20 PM ChildBfsBehaviour action
INFO: ag-mt-3 send response
Nov 03, 2021 12:27:20 PM ChildBfsBehaviour action
INFO: ag-mt-2 get response
Nov 03, 2021 12:27:20 PM ChildBfsBehaviour action
INFO: ag-mt-2 send response
Nov 03, 2021 12:27:20 PM LeaderBfsBehaviour action
INFO: get response
Nov 03, 2021 12:27:20 PM LeaderBfsBehaviour action
INFO: Computed AVERAGE: -2.5
```

## More cases to run
Run script allows generating agents networks for arbitrary number of agents.
Try to tweak script params to test different cases.

```shell
python ./scripts/run.py --agents-count=1 --agents-links=0
```

```shell
python ./scripts/run.py --agents-count=5 --agents-links=5
```

```shell
python ./scripts/run.py --agents-count=10 --agents-links=20
```

```shell
python ./scripts/run.py --agents-count=40 --agents-links=200
```

## Also

This project is done as part of `Multi-agent technologies` university course.