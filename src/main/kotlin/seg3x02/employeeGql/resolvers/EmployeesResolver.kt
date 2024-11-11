package seg3x02.employeeGql.resolvers

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
//import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.core.query.Query
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.yaml.snakeyaml.events.Event
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import java.util.*
//import javax.management.Query

@Controller
class EmployeesResolver(private val employeesRepository: EmployeesRepository,private val mongoOperatins:MongoOperations) {
    @QueryMapping
    fun employees(): List<Employee>
    {
        return employeesRepository.findAll()
    }

    @QueryMapping
    fun employeeById(@Argument("id")id:String): Employee
    {
        return employeesRepository.findById(id).orElse(null)
    }

    /*@SchemaMapping()
    fun employee(): List<Employee>
    {
        val query=Query()
        query.addCriteria(Criteria.where("id").`is`(employeesRepository.count()))
        return mongoOperatins.find(query,Employee::class.java)
    }*/

    @MutationMapping
    fun newEmployee(@Argument("createEmployeeInput")input: CreateEmployeeInput): Employee
    {
        if (input.city!=null && input.name!=null && input.gender!=null && input.salary!=null && input.dateOfBirth!=null)
        {
            val employee = Employee(input.name, input.dateOfBirth, input.city,input.salary,input.gender,input.email)
            employee.id= UUID.randomUUID().toString()
            employeesRepository.save(employee)
            return employee
        }
        else
        {
            throw Exception ("Invalid Input")
        }
    }

   /* @MutationMapping
    fun updateEmployee(@Argument("id")id: String,@Argument("createEmployeeInput")input: CreateEmployeeInput): Employee
    {
        val employee=employeesRepository.findById(id)
        employee.ifPresent {
            if (input.city != null) {
                it.city = input.city
            }
            if (input.name != null) {
                it.name = input.name
            }
            if (input.gender != null) {
                it.gender = input.gender
            }
            if (input.dateOfBirth != null) {
                it.dateOfBirth = input.dateOfBirth
            }
            if (input.salary != null) {
                it.salary = input.salary
            }
            employeesRepository.save(it)
        }
        return employee.get()

}*/

    @MutationMapping
    fun deleteEmployee(@Argument("id")id: String): Boolean
    {
        employeesRepository.deleteById(id)
        return true;
    }
}