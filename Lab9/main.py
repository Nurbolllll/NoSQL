from fastapi import FastAPI, HTTPException
from pymongo.errors import PyMongoError
from repository import CourseRepository
from models import CourseModel
import config

app = FastAPI(title="Lab 9 NoSQL API")
repo = CourseRepository(config.MONGODB_URI, config.DB_NAME)

@app.post("/courses/", status_code=201)
def create_course(course: CourseModel):
    try:
        existing = repo.find_by_code(course.code)
        if existing:
            raise HTTPException(status_code=409, detail="Course with this code already exists")
        created = repo.create(course.dict())
        return created
    except PyMongoError:
        raise HTTPException(status_code=503, detail="Storage temporarily unavailable")

@app.get("/courses/")
def get_courses():
    try:
        return repo.get_all()
    except PyMongoError:
        raise HTTPException(status_code=503, detail="Storage temporarily unavailable")

@app.get("/courses/{code}")
def get_course(code: str):
    try:
        item = repo.find_by_code(code)
        if not item:
            raise HTTPException(status_code=404, detail="Course not found")
        return item
    except PyMongoError:
        raise HTTPException(status_code=503, detail="Storage temporarily unavailable")

@app.patch("/courses/{code}/auditory")
def update_course_auditory(code: str, auditory: str):
    try:
        updated = repo.update_auditory(code, auditory)
        if not updated:
            raise HTTPException(status_code=404, detail="Course not found")
        return updated
    except PyMongoError:
        raise HTTPException(status_code=503, detail="Storage temporarily unavailable")

@app.delete("/courses/{code}")
def delete_course(code: str):
    try:
        success = repo.delete(code)
        if not success:
            raise HTTPException(status_code=404, detail="Course not found")
        return {"message": "Course deleted successfully"}
    except PyMongoError:
        raise HTTPException(status_code=503, detail="Storage temporarily unavailable")