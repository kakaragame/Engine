package org.kakara.engine.models;

import org.lwjgl.assimp.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.kakara.engine.utils.Utils.ioResourceToByteBuffer;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 */
public class SimpleAIFileOpenProc extends AIFileOpenProc {
    public long invoke(long pFileIO, long fileName, long openMode) {
        AIFile aiFile = AIFile.create();
        final ByteBuffer data;
        String fileNameUtf8 = memUTF8(fileName);
        try {
            data = ioResourceToByteBuffer(fileNameUtf8, 8192);
        } catch (IOException e) {
            throw new RuntimeException("Could not open file: " + fileNameUtf8);
        }
        AIFileReadProcI fileReadProc = new AIFileReadProc() {
            public long invoke(long pFile, long pBuffer, long size, long count) {
                long max = Math.min(data.remaining(), size * count);
                memCopy(memAddress(data) + data.position(), pBuffer, max);
                return max;
            }
        };
        AIFileSeekI fileSeekProc = new AIFileSeek() {
            public int invoke(long pFile, long offset, int origin) {
                if (origin == Assimp.aiOrigin_CUR) {
                    data.position(data.position() + (int) offset);
                } else if (origin == Assimp.aiOrigin_SET) {
                    data.position((int) offset);
                } else if (origin == Assimp.aiOrigin_END) {
                    data.position(data.limit() + (int) offset);
                }
                return 0;
            }
        };
        AIFileTellProcI fileTellProc = new AIFileTellProc() {
            public long invoke(long pFile) {
                return data.limit();
            }
        };
        aiFile.ReadProc(fileReadProc);
        aiFile.SeekProc(fileSeekProc);
        aiFile.FileSizeProc(fileTellProc);
        return aiFile.address();
    }
}
