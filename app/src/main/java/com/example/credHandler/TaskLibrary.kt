package com.example.credHandler

class AddTask(IDCount: Int, TaskT: String, TaskD: String) {
    private var taskID: Int = IDCount
    private var taskTitle: String = TaskT
    private var taskDescr: String = TaskD

    fun getTaskID(): Int {
        return taskID
    }

    fun setTaskID(id: Int) {
        taskID = id
    }

    fun setTaskTitle(tmpT: String) {
        taskTitle = tmpT
    }

    fun getTaskTitle(): String {
        return taskTitle
    }

    fun setTaskDesc(tmpD: String) {
        taskDescr = tmpD
    }

    fun getTaskDesc(): String {
        return taskDescr
    }

    fun getFormattedString(): String {
        // the format to input string into text file
        return getTaskTitle() + "|" + getTaskDesc()
    }

    fun getDspString(): String {
        return getTaskTitle()
    }
}

object TaskLib {
    // Task ID generator, ensures uniqueness
    private var totalTasks: Int = 0
    private var instantiatedObjects = mutableListOf<AddTask>()

    fun getTotalTasks(): Int {
        return totalTasks
    }

    fun trackObject(obj: AddTask) {
        instantiatedObjects.add(obj)
        totalTasks++
    }

    fun getAnyWithin(taskID: Int): AddTask {
        return instantiatedObjects.toList()[taskID]
    }

    fun getRecent(): AddTask {
        return instantiatedObjects.toList().last()
    }

    fun getAllObjects(): List<AddTask> {
        return instantiatedObjects.toList()
    }

    fun delThatObject(id: Int) {
        instantiatedObjects.removeAt(id)
        reassignTaskID()
        totalTasks--
    }

    fun delAllObject() {
        while(getAllObjects().isNotEmpty()) {
            instantiatedObjects.removeAt(0)
            totalTasks--
        }
    }

    fun prioritizeItem(taskID: Int) {
        val shifted = mutableListOf<AddTask>()
        shifted.add(instantiatedObjects[taskID])
        for(obj in instantiatedObjects) {
            if (obj != instantiatedObjects[taskID]) {
                shifted.add(obj)
            }
        }
        instantiatedObjects.clear()
        for(obj in shifted) {
            instantiatedObjects.add(obj)
        }
        reassignTaskID()
    }

    private fun reassignTaskID() {
        // this for-loop increment iterator by one for every loop
        for((iterator, obj) in getAllObjects().withIndex()) {
            obj.setTaskID(iterator)
        }
    }
}