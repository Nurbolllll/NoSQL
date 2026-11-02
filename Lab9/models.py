from pydantic import BaseModel, Field

class CourseModel(BaseModel):
    code: str = Field(..., description="Уникальный код курса, например NOSQL301")
    title: str = Field(..., description="Название курса")
    instructor: str = Field(..., description="Преподаватель")
    auditory: str = Field(..., description="Номер аудитории")