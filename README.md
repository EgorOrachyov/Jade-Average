# Jade-Average
Average of the random numbers generated in multi-agent environment.

### Algorithm
Algorithm name is `BFS from chosen leader`.
The idea of the algorithm is to choose by voting a leader based on
agents ids and the initiate bfs from leader to collect sums of children
and get the result sum in leader to send it to the `center`.

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
  - He sends request to know `(sum, n)` of its children. In request, it puts current bfs level. Initial level is 0. 
  - If agent receives request first time from some agent `k`, then he is child of `k`.
  - Agent marks himself as visited, increases bfs level and tries to send request further to his children.
  - If all children visited, then agent returns `(self.Number, 1)`.
  - Else agent waits for children, he successfully sent request.
  - For a given list of `[(sum, k)i] of size n`, agent returns `(sum_1 + .. + sum_n, k_1 + .. + k_n)`.
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

