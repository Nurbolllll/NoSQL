from pymongo import MongoClient

class CourseRepository:
    def __init__(self, uri: str, db_name: str):
        self.client = MongoClient(uri, serverSelectionTimeoutMS=3000)
        self.db = self.client[db_name]
        self.col = self.db['courses']

    def find_by_code(self, code: str):
        return self.col.find_one({'code': code}, {'_id': 0})

    def create(self, data: dict):
        self.col.insert_one(data)
        return self.find_by_code(data['code'])

    def get_all(self):
        return list(self.col.find({}, {'_id': 0}))

    def update_auditory(self, code: str, new_auditory: str):
        result = self.col.update_one({'code': code}, {'$set': {'auditory': new_auditory}})
        if result.matched_count == 0:
            return None
        return self.find_by_code(code)

    def delete(self, code: str):
        result = self.col.delete_one({'code': code})
        return result.deleted_count > 0