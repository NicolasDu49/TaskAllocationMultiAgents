**Allocation de tâches par la formation de coalitions entre agents autonomes**

Pour pouvoir exécuter le code, copiez le dossier `src` dans un workspace Eclipse vierge (ne nécessite pas de librairies supplémentaires).

Ce projet est une implémentation d'un algorithme de formation de coalitions au sein d'un environnement multi-agents, où chaque agent est autonome. Ces coalitions se forment dans le but de pouvoir réaliser une liste de tâches dont les capacités requises sont trop élevées ou complexes pour être réalisées par un agent seul.

Ce projet a été réalisé en Java et est structuré comme suit :

- **Classe Agent** : représente l'objet Agent, contient des capacités individuelles, une liste de coalitions à calculer et garde en mémoire les agents rencontrés dans le processus de négociation.
- **Classe Coalition** : représente les coalitions formées dans le programme, soit un regroupement d'agents et la concaténation de toutes leurs capacités. Une Task peut être attribuée à une Coalition.
- **Classe Task** : représente une tâche à réaliser. Elle a des caractéristiques qui sont par la suite traduites en capacités qu'une coalition doit atteindre pour pouvoir la réaliser.
- **Classe CoalitionMaker** : représente l'environnement multi-agent que l'on traite. Comporte une liste d'Agents, une liste de Tasks et une liste vide de Coalitions (représentant la configuration finale). En outre, le CoalitionMaker contient l'algorithme global de formation des coalitions et d'attribution des tâches. Il invoque tour à tour les Agents qui ont des calculs spécifiques à faire afin de déterminer, dans un processus de négociation itératif, quelle serait la configuration de coalition dégageant le meilleur score final.
