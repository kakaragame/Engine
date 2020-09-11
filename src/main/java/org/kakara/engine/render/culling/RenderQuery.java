package org.kakara.engine.render.culling;

import static org.lwjgl.opengl.GL15.*;

/**
 * Represents the OpenGL query.
 * @since 1.0-Pre3
 */
public class RenderQuery {
    private final int id;
    private final int type;
    private boolean inUse = false;

    private int previousResult = -1;

    /**
     * Create a RenderQuery of a specific type.
     * @param type The query type. (See http://docs.gl/gl4/glBeginQuery)
     */
    public RenderQuery(int type){
        this.type = type;
        this.id = glGenQueries();
    }

    /**
     * Start the query.
     * <p>Only one query can be going at a time.</p>
     */
    public void start(){
        glBeginQuery(type, id);
        inUse = true;
    }

    /**
     * End the query.
     */
    public void end(){
        glEndQuery(type);
    }

    /**
     * Check to see if the results are ready.
     * @return If the results are ready.
     */
    public boolean isResultReady(){
        return glGetQueryObjecti(id, GL_QUERY_RESULT_AVAILABLE) == GL_TRUE;
    }

    /**
     * Check to see if the query is still being calculated by the gpu.
     * <p>Note: This is not set to false until the {@link #getResult()} or {@link #pollPreviousResult()} is called.</p>
     * @return If the gpu is calculating the query.
     */
    public boolean isInUse(){
        return inUse;
    }

    /**
     * Get the result of the query.
     * <p>Note: This does NOT force the query to finish, instead the query is marked as not active if the gpu
     * did not finish the query.</p>
     * @return The result.
     */
    public int getResult(){
        inUse = false;
        return glGetQueryObjecti(id, GL_QUERY_RESULT);
    }

    /**
     * If the gpu is done with the query it will return the new result, else
     * it will return the previous result. (Default result is -1).
     * @return The previous result.
     */
    public int pollPreviousResult(){
        if(isInUse() && isResultReady()){
            previousResult = getResult();
        }
        return previousResult;
    }

    /**
     * Delete the query.
     * <p>This should be triggered automatically if this query belongs to an engine defined class.</p>
     */
    public void delete(){
        glDeleteQueries(id);
    }
}
