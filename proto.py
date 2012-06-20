import readline

import world
import geom
import entity
import lasergun

class GameState:
    world = world.World()

    def get_world(self):
        return self.world


state = None

def new_state():
    state = GameState()
    print 'new gamestate created'

def clear_state():
    state = None
    print 'gamestate cleared'

def add_entity():
    if state == None:
        print 'no state to create entity'
        return
    state.get_world().get_entites().append(new_en) 

def list_entities():
    if state == None:
        print 'no state to list entities'
        return
    i = 1
    for ent in state.get_world().get_entites():
        print '%d. %s -- %s -- %d -- %d' % (i, ent.get_name(), ent.get_inst(), ent.get_loc().x[0], ent.get_loc().x[1])

def execute_cmd(command):
    if command == 'quit' or command == 'q':
        return True
    if command == 'new':
        new_state()
    elif command == 'clear':
        clear_state()
    elif command == 'add':
        add_entity() 
    elif command == 'list':
        list_entities()
    return False

def main():
    exit = False

    while not exit:
        cmd = raw_input(']')
        exit = execute_cmd(cmd)

main()

