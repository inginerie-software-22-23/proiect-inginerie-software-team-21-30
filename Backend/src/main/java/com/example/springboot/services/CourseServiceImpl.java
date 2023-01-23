package com.example.springboot.services;

import com.example.springboot.exceptions.NoSuchCourseExistsException;
import com.example.springboot.models.Course;
import com.example.springboot.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public String create(Course course) {
        courseRepository.save(course);
        return "Course saved successfully";
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(()
                        -> new NoSuchCourseExistsException("No course found with id = " + id));
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findCoursesByMentor(String username) {
        return courseRepository.findAll().stream().
                filter(x -> x.getUser().getName().equals(username)).
                collect(Collectors.toList());
    }

    public List<Course> filterByName(String courseName) {
        return courseRepository.findAll().stream().
                filter(x -> x.getName().contains(courseName)).
                collect(Collectors.toList());
    }

    public String update(Course newCourseData, Long id) {
        Optional<Course> courseToUpdate = courseRepository.findById(id);

        if (courseToUpdate.isEmpty()) {
            throw new NoSuchCourseExistsException("No course found with id = " + id);
        }

        courseToUpdate.get().setName(newCourseData.getName());
        courseToUpdate.get().setShortDescription(newCourseData.getShortDescription());
        courseToUpdate.get().setLongDescription(newCourseData.getLongDescription());
        courseToUpdate.get().setMeetLink(newCourseData.getMeetLink());

        courseRepository.save(courseToUpdate.get());
        return "Course updated successfully";
    }

    public String delete(Long id) {
        Optional<Course> courseToDelete = courseRepository.findById(id);

        if (courseToDelete.isEmpty()) {
            throw new NoSuchCourseExistsException("No course found with id = " + id);
        }

        courseRepository.deleteById(id);
        return "Course deleted successfully";
    }
}
