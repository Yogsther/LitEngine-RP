### LitEngine RP (Role Playing Game Engine)

#### [Java Docs](https://yogsther.github.io/LitEngine-RP/)

![Screenshot of devtools](https://i.imgur.com/Mn1dib1.png)

#### Controlls: 
* WASD - Controll the player
* E - Open map-editor
	* This disabled collisions
	* Z - Reset map
	* X - Delete last placed object
	* ENTER - Place object
	* C - Copy map to clipboard
	* L - Swap object >
	* K - Swap object < 
	* T - Toggle collision
	* R - Rotate object (in-progress)
	* Up / Down arrow - Scale (in-progress)

##### TODO: 
- [ ] Directional collisions
- [ ] Add a mini-map
- [x] Swap option for objects in the map editor
- [x] Camera movement with the player
- [x] More textures and objects
- [ ] Make a bigger map

#### Creating maps and editing maps

When you have edited a map, hit C to copy the map. Then take that code and run it to load the map.

```java
/* LitEngineRP Map - Size: 7 */
Game.map.add(new GameObject("wood", -22, 40, true, 1.0f));
Game.map.add(new GameObject("wood", 4, 52, true, 1.0f));
Game.map.add(new GameObject("wood", 36, 84, true, 1.0f));
Game.map.add(new GameObject("wood", 19, 117, true, 1.0f));
Game.map.add(new GameObject("wood", -35, 117, true, 1.0f));
Game.map.add(new GameObject("wood", -65, 87, true, 1.0f));
Game.map.add(new GameObject("wood", -64, 40, true, 1.0f));
```




