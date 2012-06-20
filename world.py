import entity

class World:
    entities = []

    def get_entities(self):
        return self.entities

    def add_entity(self, ent):
        self.entities.append(ent)
