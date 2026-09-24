# Textadventure Assignment

Ziel ist es, mit Jackson YAML-Files zu verarbeiten und eine Game-Engine ala [Zork](https://www.pcjs.org/software/pcx86/game/infocom/zork1/) zu bauen. Die Projektnote ergibt sich zu 50% aus dem Produkt (Features) und zu 50% aus einem schriftlichen Kompetenznachweis / Kurzprüfung.

## YAML

```yaml
startRoom: room-one # must match one of the keys in rooms:
description: |
  Welcome to the tutorial! This is a simple text adventure (also called 'interactive fiction').
  You can: Explore different locations, pick up items (into your 'inventory') and interact with objects and the environment to solve puzzles.
  You interact with your environment by entering simple sentences, starting with a verb.
  For a list of possible verbs, enter 'help verbs'.
  Often it is useful to examine objects: 'examine table'.
  Some objects are portable, to pick up a book, enter 'take book'
  Start with exploring your environment by visiting the other room. To do so, enter 'go east'.
rooms: # Map<String, Room>
  room-one: # they key of the room, or the internal name how it is referenced
    name: Starting room
    description: | # the pipe indicates a multiline string, see https://yaml-multiline.info/
      You are in an unadorned, rectangular room without windows and a table next to the wall.
      A simple light bulb is hanging from the ceiling, the only source of light, casting harsh shadows.
      There is a passage to the east, apparently to another room much like this.
    verbs: # for example as: Map<String, Map<String, List<Action>>>
      go: # "go" is the verb
        east: # "east" is the object, what follows, is a list (with 1 item) of actions:
          - room: room-two # "room" indicates we will switch rooms, this must be a key in "rooms"
      look:
        table:
          # "message" will be printed, but has no effect otherwise (but might yield hints)
          - message: "This is a simple, wooden table. It has a small key lying on top."
        light:
          - message: "A simple light bulb. It reads: 60W."
        key:
          - message: "A small key."
      take:
        table:
          - message: "Guess what: The table is too heavy to carry around."
        light:
          - message: "How many players of interactive fiction does it take to unscrew this light bulb? Apparently, more than one."
        key:
          # conditional: only taken if a state "key" is set, otherwise the next action in the list is evaluated
          # only run the first matching action of the list
          - ifState: key
            message: "You already have the key safely in your pocket."
          # else:
          - addState: key # add a new state "key" to the Set of states
            message: "You pack it into your pocket."
  room-two:
    name: Another room
    description: |
      You are in another unadorned, rectangular room.
      There is a passage to the west. In the opposite direction you see a large wardrobe.
    verbs:
      go:
        west:
          - room: room-one
        east:
          - ifState: coat
            room: secret
      look:
        wardrobe:
          - ifState: wardrobeOpen
            message: "A large, white wardrobe with an open door. There is a black coat inside."
          - message: "A large, white wardrobe. It looks like an IKEA model. It is closed."
        coat:
          - message: "A large, black coat. It might suit you."
      open:
        wardrobe:
          - ifState: key
            message: "The wardrobe is now unlocked."
            addState: wardrobeOpen
          - message: "You try to pick up the wardrobe... Well... no."
      take:
        coat:
          - ifState: wardrobeOpen
            message: "It suits you well. But more importantly: you detect a secret passage through the wardrobe towards the east."
            addState: coat
  secret:
    name: A hidden room
    description: You found the hidden passage to Narnia. The tutorial ends here. Congratulations!
    verbs:
      go:
        west:
          - room: room-two
# global verbs: here we define defaults/synonyms and error-messages
globals:
  default:
    unknownVerb: You cannot do that.
    unknownObject: ""
  help:
    synonyms: [ wtf ]
    unknownVerb: |
        Most of the time, typing something like <verb object> works. Example: <open door>.
        You can go to a possible direction typing <go (direction)>.
        You can examine the room or any object typing <examine (object)>. Look around with <look>.
        Type <help verbs> to get a list of possible verbs.
    unknownObject: "Possible verbs include: go, take, examine, open,..."
  go:
    synonyms: [ walk, drive, climb, move ]
    unknownVerb: You can't go there! # if the room does not know the verb 'go' at all
    unknownObject: You can't go {} from here! # if the room does know 'go' but not the object
  take:
    synonyms: [ grab, get, pick ]
    unknownVerb: There's nothing to take!
    unknownObject: You can't take {}!
  look:
    synonyms: [ examine ]
    unknownVerb: Nothing to look at in here!
    unknownObject: You cannot look at {}!
  open:
    synonyms: [ unlock ]
    unknownVerb: Nothing to open here!
    unknownObject: You cannot open {}!
```

## Erwartete Features

Das `tutorial.yaml` ist spielbar. Ihre Gameengine ist generisch genug, dass sie eine beliebige .yaml-Geschichte in demselben Format abspielen kann.

1. Es gibt einen Main-Loop, und man kann den Raum wechseln. Die Geschichte wird durch eigene Räume erweitert.

2. Es wird Input von der Konsole gelesen.

3. Das komplette YAML wird korrekt deserialisiert, insbesondere:

    1. `ifState` und `addState` werden unterstützt.

    2. Es können Synonyme verwendet werden (z. B. "walk" anstatt "go").

    3. Es gibt Error-Handling entsprechend den globalen `verbs:`.

4. Code ist sinnvoll nach dem MVC-Pattern aufgeteilt.

5. Java 25 wird verwendet und deren neue Sprachfeatures werden genutzt. Das `var`-Keyword wird verwendet, wo möglich.

6. Jackson wird möglichst als einzige Library verwendet. Libraries sind auf dem neusten Stand. Gradle als Buildsystem.

7. Keine Sonarlint Warnungen, insbesondere sinnvoll kleine Methoden. Code ist englisch.

8. Es wird eine selbst gewählte, für den Nutzer sichtbare, Erweiterung implementiert. Bsp. farbige Konsole (langweilig), ein Speichersystem, History-Support, …​

Das yaml-Format darf nur in expliziter Absprache mit der LP angepasst werden und das initiale yaml-File muss immer korrekt funktionieren.

## Erwartete neue Kompetenzen

1. Sie lesen Java `records` ohne Hilfsmittel, verstehen den Bezug zu Bean-Klassen und erklären Unterschiede bzw. Gemeinsamkeiten von Konstruktoren, Zugriffsmethoden, Immutability und Value-Semantik.

2. Sie erklären die Grundlagen von Java Exception handling anhand von gegebenem Code insbesondere bezogen auf Kontrollfluss (`try`-`catch`-`finally`). Sie identifizieren Probleme durch catch-Blöcke, welche Exceptions nicht weiter propagieren.

3. Sie lesen die YAML-Syntax auswendig (ohne Alias/Anchror/Tags) und schreiben valides YAML, anhand einer Beschreibung, selbst.

4. Sie wechseln fliessend zwischen ähnlichen Objekten in beliebigen, vorgegebenen Serialisierungs- und DTO-Formaten und erklären dieses Zusammenspiel: Bsp. JSON→Java-Klasse, YAML→XML, usw.

5. Sie können ihren eigenen Code präsentieren und zeigen, wie der mit dem YAML zusammenhängt.

6. Sie erkennen ein MVC-Pattern bei gegebenem Code und machen sinnvolle Vorschläge für ein MVC-Pattern, bei einer gegebenen Situation.

7. Sie nutzen einfache AI Agenten und prompten spezifisch und effizient in Fachsprache.

8. Sie wählen die Rolle der KI in ihrer Diskussion bewusst:

    1. Lernen: Erkläre mir das Konzept von …

    2. Beispiel erhalten: Erstelle mir ein weiteres Beispiel zu …

    3. Kontrolle: Prüfe den Code auf …

    4. Fehleranalyse: Erkläre mir diesen Stacktrace …

    5. Ideen entwickeln: Was gäbe es für zustzliche Varianten um …

    6. Reflexion: Bewerte meine Lösung kritisch und mach Verbesserungsvorschläge zu …

## Abgabe

1. Ihr komplettes Projekt in einem **privaten** GitHub-Repository mit mindestens einem Commit pro Arbeitswoche.

2. Eine (generierte) Zusammenfassung benutzter Prompts für das Erarbeiten dieser Lösung (`history.md`).

3. Ein `AGENTS.md` File mit ihren persönlichen gesammelten Regeln für ihren Agenten in diesem Projekt.

4. Eine persönliche Beschreibung des Projektes (`Selbstevaluation.md`) im Umfang von mindestens 200 Wörtern. Diese ist händisch zu schreiben und beinhaltet mindestens:

    1. Eine persönliche Wertung des Projektes: Was war sinnvoll / lehrreich / aufwändig / erstaunlich / …​?

    2. Wie ist Ihre persönliche Wertung von agentischem Programmieren?

    3. Eine Beschreibung vorhandener Zusatzfeatures im Produkt und worauf Sie besonders stolz sind.

    4. Wie sie vorgegangen sind, um die geforderten Kompetenzen zu erreichen.

## AI Nutzung

Ist **vorgegeben**: Es soll möglichst wenig Code von Hand angepasst werden.

AI muss in der IDE integriert genutzt werden: Wir wollen kein Chat-Copy-Paste. GitHub-Copilot Lizenzen sind via BBW verfügbar, noch bessere Resultate bekommen Sie (leider) generell mit einem bezahlten Claude/Codex Abo. Dadurch, dass AI frei genutzt werden soll, ist ein perfektes Produkt erwartet. Generell wird das Produkt von der LP nur automatisiert validiert, es sei denn, ein Codereview auf selbst geschriebenem Code wird gewünscht.

Die Kompetenzen können (und sollen) auch ausserhalb des Produktkontextes erworben werden.

- [https://education.github.com/pack](https://education.github.com/pack) signup mit Legi

- [https://dlh.zh.ch/home/genki/lehren-und-lernen-mit-ki/prompting](https://dlh.zh.ch/home/genki/lehren-und-lernen-mit-ki/prompting)

---

260923-a6547c5c

Dieses Dokument © 2026 [Berufsbildungsschule Winterthur](https://bbw.ch). Lizenziert unter [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/).
