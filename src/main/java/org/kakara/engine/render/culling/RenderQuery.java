package org.kakara.engine.render.culling;

import static org.lwjgl.opengl.GL15.*;

/**
 * Represents and OpenGL query.
 * @since 1.0-Pre3
 * TODO Comment this class.
 */
public class RenderQuery {
    private final int id;
    private final int type;
    private boolean inUse = false;

    private int previousResult = -1;

    public RenderQuery(int type){
        this.type = type;
        this.id = glGenQueries();
    }

    public void start(){
        glBeginQuery(type, id);
        inUse = true;
    }

    public void end(){
        glEndQuery(type);
    }

    public boolean isResultReady(){
        return glGetQueryObjecti(id, GL_QUERY_RESULT_AVAILABLE) == GL_TRUE;
    }

    public boolean isInUse(){
        return inUse;
    }

    public int getResult(){
        inUse = false;
        return glGetQueryObjecti(id, GL_QUERY_RESULT);
    }

    public int pollPreviousResult(){
        if(isInUse() && isResultReady()){
            previousResult = getResult();
        }
        return previousResult;
    }

    public void delete(){
        glDeleteQueries(id);
    }
}
